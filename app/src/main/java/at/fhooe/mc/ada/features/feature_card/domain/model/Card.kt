package at.fhooe.mc.ada.features.feature_card.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.fhooe.mc.ada.ui.theme.*

@Entity
data class Card(
    val cardName: String,
    val cardHolderName: String,
    val cardNumber: String,
    val securityNumber: String,
    val expirationDate: String,
    val isLocked: Boolean,
    val cardStyle: Int,
    val dateAdded: Long,
    val cardChipColor: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val cardStyles =
            listOf(NegativeRed, CardColorGray, CardColorGreen, CardColorOrange, CardColorPurple, CardColorBlue)
    }
}

class InvalidCardException(message: String) : Exception(message)