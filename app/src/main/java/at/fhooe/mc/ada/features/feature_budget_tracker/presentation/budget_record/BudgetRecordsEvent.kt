package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record

import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.util.BudgetRecordOrder

sealed class BudgetRecordsEvent {
    data class Order(val budgetRecordOrder: BudgetRecordOrder) : BudgetRecordsEvent()
    data class DeleteNode(val budgetRecord: BudgetRecord) : BudgetRecordsEvent()
}