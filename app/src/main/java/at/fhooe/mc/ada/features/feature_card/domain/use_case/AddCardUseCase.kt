package at.fhooe.mc.ada.features.feature_card.domain.use_case

import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.model.InvalidCardException
import at.fhooe.mc.ada.features.feature_card.domain.repository.CardRepository

class AddCardUseCase(private val repository: CardRepository) {

    @Throws(InvalidCardException::class)
    suspend operator fun invoke(card: Card) {
        if (card.cardName.isBlank()) {
            throw InvalidCardException("The card name can't be empty!")
        }
        repository.insertCard(card)
    }
}