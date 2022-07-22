package at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.fhooe.mc.ada.R
import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.model.InvalidCardException
import at.fhooe.mc.ada.features.feature_card.domain.use_case.CardUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCardViewModel @Inject constructor(
    private val cardUseCases: CardUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _cardName = mutableStateOf("")
    val cardName: State<String> = _cardName

    private val _cardCurrentBalance = mutableStateOf(0.0)
    val cardCurrentBalance: State<Double> = _cardCurrentBalance

    private val _cardIsLocked = mutableStateOf(false)
    val cardIsLocked: State<Boolean> = _cardIsLocked

    private val _cardImage = mutableStateOf(R.drawable.test_card)
    val cardImage: State<Int> = _cardImage

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentCardId: Int? = null

    init {
        savedStateHandle.get<Int>("cardId")?.let { cardId ->
            if (cardId != -1) {
                viewModelScope.launch {
                    cardUseCases.getCard(cardId)?.also { card ->
                        currentCardId = card.id
                        _cardName.value = card.cardName
                        _cardCurrentBalance.value = card.currentBalance ?: 0.00
                        _cardImage.value = card.image ?: R.drawable.test_card
                        _cardIsLocked.value = card.isLocked
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditCardEvent) {
        when (event) {
            is AddEditCardEvent.EnteredCardName -> {
                _cardName.value = event.cardName
            }
            is AddEditCardEvent.EnteredCurrentBalance -> {
                _cardCurrentBalance.value = event.currentBalance
            }
            is AddEditCardEvent.ChangeIsLocked -> {
                _cardIsLocked.value = event.isLocked
            }
            is AddEditCardEvent.ChangeImage -> {
                _cardImage.value = event.image
            }
            is AddEditCardEvent.SaveCard -> {
                viewModelScope.launch {
                    try {
                        cardUseCases.addCard(
                            Card(
                                cardName = cardName.value,
                                currentBalance = cardCurrentBalance.value,
                                isLocked = cardIsLocked.value,
                                image = cardImage.value,
                                dateAdded = System.currentTimeMillis(),
                                id = currentCardId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveCard)
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
        object SaveCard : UiEvent()
    }
}