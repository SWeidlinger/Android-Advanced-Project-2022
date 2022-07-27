package at.fhooe.mc.ada.features.feature_budget_tracker.domain.repository

import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getBudgetRecords(): Flow<List<BudgetRecord>>

    suspend fun getBudgetRecordById(id: Int): BudgetRecord?

    suspend fun insertBudgetRecord(budgetRecord: BudgetRecord)

    suspend fun deleteBudgetRecord(budgetRecord: BudgetRecord)
}