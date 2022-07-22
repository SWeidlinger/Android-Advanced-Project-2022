package at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card

sealed class AddEditCardEvent {
    data class EnteredCardName(val cardName: String) : AddEditCardEvent()
    data class EnteredCurrentBalance(val currentBalance: Double) : AddEditCardEvent()
    data class ChangeIsLocked(val isLocked: Boolean) : AddEditCardEvent()
    data class ChangeImage(val image: Int) : AddEditCardEvent()
    object SaveCard: AddEditCardEvent()
}