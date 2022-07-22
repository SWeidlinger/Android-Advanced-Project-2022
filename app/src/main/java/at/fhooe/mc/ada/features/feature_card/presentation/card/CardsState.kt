package at.fhooe.mc.ada.features.feature_card.presentation.card

import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.util.CardOrder
import at.fhooe.mc.ada.features.feature_card.domain.util.OrderType

data class CardsState(
    val cards: List<Card> = emptyList(),
    val cardOrder: CardOrder = CardOrder.DateAdded(OrderType.Ascending)
)
