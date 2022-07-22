package at.fhooe.mc.ada.features.feature_card.data.data_source

import androidx.room.*
import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM card")
    fun getCards(): Flow<List<Card>>

    @Query("SELECT * from card WHERE id = :id")
    suspend fun getCardByID(id: Int): Card?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)
}