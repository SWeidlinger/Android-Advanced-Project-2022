package at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card

sealed class AddEditCardEvent {
    data class EnteredCardName(val cardName: String) : AddEditCardEvent()
    data class EnteredCardHolderName(val cardHolderName: String) : AddEditCardEvent()
    data class EnteredCardNumber(val cardNumber: String) : AddEditCardEvent()
    data class EnteredSecurityNumber(val securityNumber: String) : AddEditCardEvent()
    data class EnteredExpirationDate(val expirationDate: String) : AddEditCardEvent()
    data class ChangeIsLocked(val isLocked: Boolean) : AddEditCardEvent()
    data class ChangeCardStyle(val style: Int) : AddEditCardEvent()
    object SaveCard : AddEditCardEvent()
}