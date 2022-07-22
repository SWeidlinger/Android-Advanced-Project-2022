package at.fhooe.mc.ada.features.feature_card.domain.use_case

import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.repository.CardRepository

class DeleteCardUseCase(private val repository: CardRepository) {
    suspend operator fun invoke(card: Card) {
        repository.deleteCard(card)
    }
}