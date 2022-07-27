package at.fhooe.mc.ada.features.feature_budget_tracker.domain.util

import at.fhooe.mc.ada.core.domain.util.OrderType

sealed class BudgetRecordOrder(val orderType: OrderType) {
    class BudgetRecordName(orderType: OrderType) : BudgetRecordOrder(orderType)
    class BudgetRecordAmount(orderType: OrderType) : BudgetRecordOrder(orderType)
    class BudgetRecordDate(orderType: OrderType) : BudgetRecordOrder(orderType)

    fun copy(orderType: OrderType): BudgetRecordOrder {
        return when (this) {
            is BudgetRecordName -> BudgetRecordName(orderType)
            is BudgetRecordAmount -> BudgetRecordAmount(orderType)
            is BudgetRecordDate -> BudgetRecordDate(orderType)
        }
    }
}
