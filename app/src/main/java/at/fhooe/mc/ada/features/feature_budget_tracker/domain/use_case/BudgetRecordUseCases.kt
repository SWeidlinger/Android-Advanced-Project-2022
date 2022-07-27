package at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case

data class BudgetRecordUseCases(
    val getBudgetRecords: GetBudgetRecordsUseCase,
    val deleteBudgetRecord: DeleteBudgetRecordUseCase,
    val addBudgetRecord: AddBudgetRecordUseCase,
    val getBudgetRecord: GetBudgetRecordUseCase
)
