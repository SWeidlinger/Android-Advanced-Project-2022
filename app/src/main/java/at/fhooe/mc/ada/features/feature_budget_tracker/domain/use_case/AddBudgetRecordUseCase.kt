package at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case

import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.InvalidBudgetRecordException
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.repository.BudgetRepository
import at.fhooe.mc.ada.features.feature_card.domain.model.InvalidCardException

class AddBudgetRecordUseCase(private val repository: BudgetRepository) {
    @Throws(InvalidBudgetRecordException::class)
    suspend operator fun invoke(budgetRecord: BudgetRecord) {
        if (budgetRecord.budgetRecordName.isBlank()) {
            throw InvalidCardException("The card name can't be empty!")
        }
        repository.insertBudgetRecord(budgetRecord)
    }
}