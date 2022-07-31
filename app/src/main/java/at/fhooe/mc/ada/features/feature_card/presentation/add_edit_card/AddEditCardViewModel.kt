package at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import kotlin.random.Random

@HiltViewModel
class AddEditCardViewModel @Inject constructor(
    private val cardUseCases: CardUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _cardName = mutableStateOf("")
    val cardName: State<String> = _cardName

    private val _cardHolderName = mutableStateOf("")
    val cardHolderName: State<String> = _cardHolderName

    private val _cardNumber = mutableStateOf("")
    val cardNumber: State<String> = _cardNumber

    private val _cardSecurityNumber = mutableStateOf("")
    val cardSecurityNumber: State<String> = _cardSecurityNumber

    private val _cardExpirationDate = mutableStateOf("")
    val cardExpirationDate: State<String> = _cardExpirationDate

    private val _cardIsLocked = mutableStateOf(false)
    val cardIsLocked: State<Boolean> = _cardIsLocked

    private val _cardStyle = mutableStateOf(Card.cardStyles[0].toArgb())
    val cardStyle: State<Int> = _cardStyle

    private val _cardChipColor = mutableStateOf(R.drawable.credit_card_chip_gold1)
    val cardChipColor: State<Int> = _cardChipColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentCardId: Int? = null

    init {
        savedStateHandle.get<Int>("cardId")?.let { cardId ->
            if (cardId != -1) {
                viewModelScope.launch {
                    cardUseCases.getCard(cardId)?.also { card ->
                        currentCardId = card.id
                        _cardName.value = card.cardName
                        _cardHolderName.value = card.cardHolderName
                        _cardStyle.value = card.cardStyle
                        _cardIsLocked.value = card.isLocked
                        _cardNumber.value = card.cardNumber
                        _cardHolderName.value = card.cardHolderName
                        _cardSecurityNumber.value = card.securityNumber
                        _cardExpirationDate.value = card.expirationDate
                        _cardChipColor.value = card.cardChipColor
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
            is AddEditCardEvent.EnteredCardHolderName -> {
                _cardHolderName.value = event.cardHolderName
            }
            is AddEditCardEvent.ChangeIsLocked -> {
                _cardIsLocked.value = event.isLocked
            }
            is AddEditCardEvent.ChangeCardStyle -> {
                _cardStyle.value = event.style
            }
            is AddEditCardEvent.EnteredCardNumber -> {
                _cardNumber.value = event.cardNumber
            }
            is AddEditCardEvent.EnteredExpirationDate -> {
                _cardExpirationDate.value = event.expirationDate
            }
            is AddEditCardEvent.EnteredSecurityNumber -> {
                _cardSecurityNumber.value = event.securityNumber
            }
            is AddEditCardEvent.SaveCard -> {
                viewModelScope.launch {
                    try {
                        cardUseCases.addCard(
                            Card(
                                cardName = cardName.value,
                                cardHolderName = cardHolderName.value,
                                isLocked = cardIsLocked.value,
                                cardStyle = cardStyle.value,
                                dateAdded = System.currentTimeMillis(),
                                id = currentCardId,
                                cardNumber = cardNumber.value,
                                securityNumber = cardSecurityNumber.value,
                                expirationDate = cardExpirationDate.value,
                                cardChipColor = Random.nextInt(5)
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