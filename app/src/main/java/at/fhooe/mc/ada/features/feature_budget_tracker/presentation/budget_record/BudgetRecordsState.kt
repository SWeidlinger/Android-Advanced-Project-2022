package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record

import at.fhooe.mc.ada.core.domain.util.OrderType
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.util.BudgetRecordOrder

data class BudgetRecordsState(
    val budgetRecords: List<BudgetRecord> = emptyList(),
    val budgetRecordOrder: BudgetRecordOrder = BudgetRecordOrder.BudgetRecordDate(OrderType.Descending)
)
