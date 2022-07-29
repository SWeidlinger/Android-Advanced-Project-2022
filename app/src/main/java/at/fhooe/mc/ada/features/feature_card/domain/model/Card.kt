package at.fhooe.mc.ada.features.feature_card.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val cardStyles =
            listOf(Color.Red, Color.Gray, Color.Green, Color.Blue, Color.Cyan, Color.Black)
    }
}

class InvalidCardException(message: String) : Exception(message)