package at.fhooe.mc.ada.di

import android.app.Application
import androidx.room.Room
import at.fhooe.mc.ada.features.feature_budget_tracker.data.data_source.BudgetDatabase
import at.fhooe.mc.ada.features.feature_budget_tracker.data.repository.BudgetRepositoryImplementation
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.repository.BudgetRepository
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case.*
import at.fhooe.mc.ada.features.feature_card.data.data_source.CardDatabase
import at.fhooe.mc.ada.features.feature_card.data.repository.CardRepositoryImplementation
import at.fhooe.mc.ada.features.feature_card.domain.repository.CardRepository
import at.fhooe.mc.ada.features.feature_card.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModuleBudgetRecord {
    @Provides
    @Singleton
    fun provideBudgetDatabase(app: Application): BudgetDatabase {
        return Room.databaseBuilder(
            app,
            BudgetDatabase::class.java,
            BudgetDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideBudgetRepository(db: BudgetDatabase): BudgetRepository {
        return BudgetRepositoryImplementation(db.budgetDao)
    }

    @Provides
    @Singleton
    fun provideBudgetRecordUseCases(repository: BudgetRepository): BudgetRecordUseCases {
        return BudgetRecordUseCases(
            getBudgetRecords = GetBudgetRecordsUseCase(repository),
            deleteBudgetRecord = DeleteBudgetRecordUseCase(repository),
            addBudgetRecord = AddBudgetRecordUseCase(repository),
            getBudgetRecord = GetBudgetRecordUseCase(repository)
        )
    }
}