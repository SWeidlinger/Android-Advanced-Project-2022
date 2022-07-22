package at.fhooe.mc.ada.features.feature_card.domain.use_case

import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.repository.CardRepository

class GetCardUseCase(private val repository: CardRepository) {

    suspend operator fun invoke(id: Int): Card? {
        return repository.getCardByID(id)
    }
}