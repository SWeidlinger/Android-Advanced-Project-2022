package at.fhooe.mc.ada.features.feature_budget_tracker.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord


@Database(
    entities = [BudgetRecord::class],
    version = 1
)

abstract class BudgetDatabase : RoomDatabase() {
    abstract val budgetDao: BudgetDao

    companion object {
        const val DATABASE_NAME = "budget_db"
    }
}