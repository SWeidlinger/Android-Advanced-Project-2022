package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.add_edit_budget_record

sealed class AddEditBudgetRecordEvent {
    data class EnteredBudgetRecordName(val budgetRecordName: String) : AddEditBudgetRecordEvent()
    data class EnteredBudgetRecordDate(val budgetRecordDate: String) : AddEditBudgetRecordEvent()
    data class EnteredBudgetRecordAmount(val budgetRecordAmount: String) :
        AddEditBudgetRecordEvent()

    data class ChangedIsBudgetRecordExpense(val isBudgetRecordExpense: Boolean) :
        AddEditBudgetRecordEvent()

    object SaveBudgetRecord : AddEditBudgetRecordEvent()
}