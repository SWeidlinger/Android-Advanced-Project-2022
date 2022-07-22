package at.fhooe.mc.ada.features.feature_card.domain.use_case

data class CardUseCases(
    val getCards: GetCardsUseCase,
    val deleteCard: DeleteCardUseCase,
    val addCard: AddCardUseCase,
    val getCard: GetCardUseCase
)
