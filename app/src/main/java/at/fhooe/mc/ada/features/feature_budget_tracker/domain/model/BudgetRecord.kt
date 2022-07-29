package at.fhooe.mc.ada.features.feature_budget_tracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BudgetRecord(
    val budgetRecordName: String,
    val budgetRecordDate: String,
    val budgetRecordAmount: String,
    var isBudgetRecordExpense: Boolean,
    @PrimaryKey var id: Int? = null
)

class InvalidBudgetRecordException(message: String) : Exception(message)