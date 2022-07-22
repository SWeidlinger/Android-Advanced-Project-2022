package at.fhooe.mc.ada.features.feature_card.data.repository

import at.fhooe.mc.ada.features.feature_card.data.data_source.CardDao
import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow

class CardRepositoryImplementation(private val dao: CardDao) : CardRepository {
    override fun getCards(): Flow<List<Card>> {
        return dao.getCards()
    }

    override suspend fun getCardByID(id: Int): Card? {
        return dao.getCardByID(id)
    }

    override suspend fun insertCard(card: Card) {
        dao.insertCard(card)
    }

    override suspend fun deleteCard(card: Card) {
        dao.deleteCard(card)
    }
}