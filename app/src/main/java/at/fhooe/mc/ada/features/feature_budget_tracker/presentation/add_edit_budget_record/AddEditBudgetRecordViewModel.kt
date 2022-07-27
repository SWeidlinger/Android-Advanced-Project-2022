package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.add_edit_budget_record

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case.BudgetRecordUseCases
import at.fhooe.mc.ada.features.feature_card.domain.model.InvalidCardException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditBudgetRecordViewModel @Inject constructor(
    private val budgetRecordUseCases: BudgetRecordUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _budgetRecordName = mutableStateOf("")
    val budgetRecordName: State<String> = _budgetRecordName

    private val _budgetRecordDate = mutableStateOf("")
    val budgetRecordDate: State<String> = _budgetRecordDate

    private val _budgetRecordAmount = mutableStateOf(0.0)
    val budgetRecordAmount: State<Double> = _budgetRecordAmount

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentBudgetRecordId: Int? = null

    init {
        savedStateHandle.get<Int>("budgetRecordId")?.let { budgetRecordId ->
            if (budgetRecordId != -1) {
                viewModelScope.launch {
                    budgetRecordUseCases.getBudgetRecord(budgetRecordId)?.also { budgetRecord ->
                        currentBudgetRecordId = budgetRecord.id
                        _budgetRecordName.value = budgetRecord.budgetRecordName
                        _budgetRecordDate.value = budgetRecord.budgetRecordDate
                        _budgetRecordAmount.value = budgetRecord.budgetRecordAmount
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditBudgetRecordEvent) {
        when (event) {
            is AddEditBudgetRecordEvent.EnteredBudgetRecordName -> {
                _budgetRecordName.value = event.budgetRecordName
            }
            is AddEditBudgetRecordEvent.EnteredBudgetRecordDate -> {
                _budgetRecordDate.value = event.budgetRecordDate
            }
            is AddEditBudgetRecordEvent.EnteredBudgetRecordAmount -> {
                _budgetRecordAmount.value = event.budgetRecordAmount
            }
            is AddEditBudgetRecordEvent.SaveBudgetRecord -> {
                viewModelScope.launch {
                    try {
                        budgetRecordUseCases.addBudgetRecord(
                            BudgetRecord(
                                budgetRecordName = budgetRecordName.value,
                                budgetRecordDate = budgetRecordDate.value,
                                budgetRecordAmount = budgetRecordAmount.value,
                                id = currentBudgetRecordId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveBudgetRecord)
                    } catch (e: InvalidCardException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save card"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveBudgetRecord : UiEvent()
    }
}