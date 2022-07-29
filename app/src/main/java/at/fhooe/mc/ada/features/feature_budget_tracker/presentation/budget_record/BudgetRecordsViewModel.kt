package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.fhooe.mc.ada.core.domain.util.OrderType
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case.BudgetRecordUseCases
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.util.BudgetRecordOrder
import at.fhooe.mc.ada.features.feature_card.domain.util.CardOrder
import at.fhooe.mc.ada.features.feature_card.presentation.card.CardsEvent
import at.fhooe.mc.ada.features.feature_card.presentation.card.CardsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetRecordsViewModel @Inject constructor(
    private val budgetRecordUseCases: BudgetRecordUseCases
) : ViewModel() {
    private val _state = mutableStateOf(BudgetRecordsState())
    val state: State<BudgetRecordsState> = _state

    private var recentlyDeletedBudgetRecord: BudgetRecord? = null

    private var getBudgetRecordsJob: Job? = null

    init {
        getBudgetRecords(BudgetRecordOrder.BudgetRecordDate(OrderType.Descending))
    }

    fun onEvent(event: BudgetRecordsEvent) {
        when (event) {
            is BudgetRecordsEvent.Order -> {
                if (state.value.budgetRecordOrder::class == event.budgetRecordOrder::class &&
                    state.value.budgetRecordOrder.orderType == event.budgetRecordOrder.orderType
                ) {
                    return
                }
                getBudgetRecords(event.budgetRecordOrder)
            }
            is BudgetRecordsEvent.DeleteNode -> {
                viewModelScope.launch {
                    budgetRecordUseCases.deleteBudgetRecord(event.budgetRecord)
                    recentlyDeletedBudgetRecord = event.budgetRecord
                }
            }
            is BudgetRecordsEvent.RestoreRecord -> {
                viewModelScope.launch {
                    budgetRecordUseCases.addBudgetRecord(
                        recentlyDeletedBudgetRecord ?: return@launch
                    )
                    recentlyDeletedBudgetRecord = null
                }
            }
        }
    }

    private fun getBudgetRecords(budgetRecordOrder: BudgetRecordOrder) {
        getBudgetRecordsJob?.cancel()
        getBudgetRecordsJob =
            budgetRecordUseCases.getBudgetRecords(budgetRecordOrder).onEach { budgetRecords ->
                _state.value = state.value.copy(
                    budgetRecords = budgetRecords,
                    budgetRecordOrder = budgetRecordOrder
                )
            }.launchIn(viewModelScope)
    }
}