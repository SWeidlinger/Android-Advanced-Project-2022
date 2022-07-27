package at.fhooe.mc.ada.features.feature_budget_tracker.data.data_source

import androidx.room.*
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgetRecord")
    fun getBudgetRecords(): Flow<List<BudgetRecord>>

    @Query("SELECT * from budgetRecord WHERE id = :id")
    suspend fun getBudgetRecordById(id: Int): BudgetRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetRecord(budgetRecord: BudgetRecord)

    @Delete
    suspend fun deleteBudgetRecord(budgetRecord: BudgetRecord)
}