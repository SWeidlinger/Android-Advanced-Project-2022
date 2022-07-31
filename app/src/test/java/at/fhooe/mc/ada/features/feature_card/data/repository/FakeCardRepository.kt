package at.fhooe.mc.ada.features.feature_card.data.repository

import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCardRepository : CardRepository {

    private val cards = mutableListOf<Card>()

    override fun getCards(): Flow<List<Card>> {
        return flow { emit(cards) }
    }

    override suspend fun getCardByID(id: Int): Card? {
        return cards.find { it.id == id }
    }

    override suspend fun insertCard(card: Card) {
        cards.add(card)
    }

    override suspend fun deleteCard(card: Card) {
        cards.remove(card)
    }
}