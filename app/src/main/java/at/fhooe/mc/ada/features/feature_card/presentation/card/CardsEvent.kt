package at.fhooe.mc.ada.features.feature_card.presentation.card

import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.util.CardOrder

sealed class CardsEvent {
    data class Order(val cardOrder: CardOrder) : CardsEvent()
    data class DeleteNode(val card: Card) : CardsEvent()
    object RestoreCard: CardsEvent()
}
