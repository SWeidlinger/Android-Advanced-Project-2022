package at.fhooe.mc.ada.features.feature_card.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import at.fhooe.mc.ada.features.feature_card.domain.model.Card

@Database(
    entities = [Card::class],
    version = 1
)
abstract class CardDatabase : RoomDatabase() {

    abstract val cardDao: CardDao

    companion object {
        const val DATABASE_NAME = "card_db"
    }
}