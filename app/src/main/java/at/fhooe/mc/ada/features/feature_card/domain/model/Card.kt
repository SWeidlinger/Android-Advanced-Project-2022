package at.fhooe.mc.ada.features.feature_card.domain.model

import androidx.compose.ui.graphics.painter.Painter
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.Exception
import java.util.*

@Entity
data class Card(
    val cardName: String,
    val currentBalance: Double?,
    val isLocked: Boolean,
    val image: Int?,
    val dateAdded: Long,
    @PrimaryKey val id: Int? = null
)

class InvalidCardException(message: String) : Exception(message)