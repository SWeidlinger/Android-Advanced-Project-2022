package at.fhooe.mc.ada.features.feature_card.presentation.card

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.fhooe.mc.ada.features.feature_card.domain.use_case.CardUseCases
import at.fhooe.mc.ada.features.feature_card.domain.util.CardOrder
import at.fhooe.mc.ada.core.domain.util.OrderType
import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardUseCases: CardUseCases
) : ViewModel() {

    private val _state = mutableStateOf(CardsState())
    val state: State<CardsState> = _state

    private var recentlyDeletedCard: Card? = null

    private var getCardsJob: Job? = null

    init {
        getCards(CardOrder.DateAdded(OrderType.Ascending))
    }

    fun onEvent(event: CardsEvent) {
        when (event) {
            is CardsEvent.Order -> {
                if (state.value.cardOrder::class == event.cardOrder::class &&
                    state.value.cardOrder.orderType == event.cardOrder.orderType
                ) {
                    return
                }
                getCards(event.cardOrder)
            }
            is CardsEvent.DeleteNode -> {
                viewModelScope.launch {
                    cardUseCases.deleteCard(event.card)
                    recentlyDeletedCard = event.card
                }
            }
            is CardsEvent.RestoreCard -> {
                viewModelScope.launch {
                    cardUseCases.addCard(recentlyDeletedCard ?: return@launch)
                    recentlyDeletedCard = null
                }
            }
        }
    }

    private fun getCards(cardOrder: CardOrder) {
        getCardsJob?.cancel()
        getCardsJob = cardUseCases.getCards(cardOrder).onEach { cards ->
            _state.value = state.value.copy(
                cards = cards,
                cardOrder = cardOrder
            )
        }.launchIn(viewModelScope)
    }
}