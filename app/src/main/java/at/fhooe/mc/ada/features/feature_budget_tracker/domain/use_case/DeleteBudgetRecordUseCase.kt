package at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case

import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.repository.BudgetRepository

class DeleteBudgetRecordUseCase(private val repository: BudgetRepository) {
    suspend operator fun invoke(budgetRecord: BudgetRecord) {
        repository.deleteBudgetRecord(budgetRecord)
    }
}