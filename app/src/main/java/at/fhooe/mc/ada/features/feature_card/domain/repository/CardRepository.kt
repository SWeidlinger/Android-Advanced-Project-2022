package at.fhooe.mc.ada.features.feature_card.domain.repository

import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import kotlinx.coroutines.flow.Flow

interface CardRepository {

    fun getCards(): Flow<List<Card>>

    suspend fun getCardByID(id: Int): Card?

    suspend fun insertCard(card: Card)

    suspend fun deleteCard(card: Card)
}