package at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case

import at.fhooe.mc.ada.core.domain.util.OrderType
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.repository.BudgetRepository
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.util.BudgetRecordOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBudgetRecordsUseCase(private val repository: BudgetRepository) {
    operator fun invoke(
        budgetRecordOrder: BudgetRecordOrder = BudgetRecordOrder.BudgetRecordDate(OrderType.Descending)
    ): Flow<List<BudgetRecord>> {
        return repository.getBudgetRecords().map { budgetRecord ->
            when (budgetRecordOrder.orderType) {
                is OrderType.Ascending -> {
                    when (budgetRecordOrder) {
                        is BudgetRecordOrder.BudgetRecordName -> budgetRecord.sortedBy { it.budgetRecordName.lowercase() }
                        is BudgetRecordOrder.BudgetRecordDate -> budgetRecord.sortedBy { it.budgetRecordDate.lowercase() }
                        is BudgetRecordOrder.BudgetRecordAmount -> budgetRecord.sortedBy { it.budgetRecordAmount }
                    }
                }
                is OrderType.Descending -> {
                    when (budgetRecordOrder) {
                        is BudgetRecordOrder.BudgetRecordName -> budgetRecord.sortedByDescending { it.budgetRecordName.lowercase() }
                        is BudgetRecordOrder.BudgetRecordDate -> budgetRecord.sortedByDescending { it.budgetRecordDate.lowercase() }
                        is BudgetRecordOrder.BudgetRecordAmount -> budgetRecord.sortedByDescending { it.budgetRecordAmount }
                    }
                }
            }
        }
    }
}