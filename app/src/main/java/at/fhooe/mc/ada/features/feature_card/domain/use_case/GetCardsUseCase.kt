package at.fhooe.mc.ada.features.feature_card.domain.use_case

import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.repository.CardRepository
import at.fhooe.mc.ada.features.feature_card.domain.util.CardOrder
import at.fhooe.mc.ada.core.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCardsUseCase(private val repository: CardRepository) {
    operator fun invoke(
        cardOrder: CardOrder = CardOrder.DateAdded(OrderType.Ascending)
    ): Flow<List<Card>> {
        return repository.getCards().map { cards ->
            when (cardOrder.orderType) {
                is OrderType.Ascending -> {
                    when (cardOrder) {
                        is CardOrder.CardName -> cards.sortedBy { it.cardName.lowercase() }
                        is CardOrder.CardHolderName -> cards.sortedBy { it.cardHolderName.lowercase() }
                        is CardOrder.DateAdded -> cards.sortedBy { it.dateAdded }
                    }
                }
                is OrderType.Descending -> {
                    when (cardOrder) {
                        is CardOrder.CardName -> cards.sortedByDescending { it.cardName.lowercase() }
                        is CardOrder.CardHolderName -> cards.sortedByDescending { it.cardHolderName.lowercase() }
                        is CardOrder.DateAdded -> cards.sortedByDescending { it.dateAdded }
                    }
                }
            }
        }
    }
}