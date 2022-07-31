package at.fhooe.mc.ada.di

import android.app.Application
import androidx.room.Room
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
object TestAppModuleCard {

    @Provides
    @Singleton
    fun provideCardDatabase(app: Application): CardDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            CardDatabase::class.java,
        ).build()
    }

    @Provides
    @Singleton
    fun provideCardRepository(db: CardDatabase): CardRepository {
        return CardRepositoryImplementation(db.cardDao)
    }

    @Provides
    @Singleton
    fun provideCardUseCases(repository: CardRepository): CardUseCases {
        return CardUseCases(
            getCards = GetCardsUseCase(repository),
            deleteCard = DeleteCardUseCase(repository),
            addCard = AddCardUseCase(repository),
            getCard = GetCardUseCase(repository)
        )
    }
}