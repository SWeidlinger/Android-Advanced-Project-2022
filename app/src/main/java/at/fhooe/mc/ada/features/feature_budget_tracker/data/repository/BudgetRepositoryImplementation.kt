package at.fhooe.mc.ada.features.feature_budget_tracker.data.repository

import at.fhooe.mc.ada.features.feature_budget_tracker.data.data_source.BudgetDao
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class BudgetRepositoryImplementation(private val dao: BudgetDao) : BudgetRepository {
    override fun getBudgetRecords(): Flow<List<BudgetRecord>> {
        return dao.getBudgetRecords()
    }

    override suspend fun getBudgetRecordById(id: Int): BudgetRecord? {
        return dao.getBudgetRecordById(id)
    }

    override suspend fun insertBudgetRecord(budgetRecord: BudgetRecord) {
        dao.insertBudgetRecord(budgetRecord)
    }

    override suspend fun deleteBudgetRecord(budgetRecord: BudgetRecord) {
        dao.deleteBudgetRecord(budgetRecord)
    }
}