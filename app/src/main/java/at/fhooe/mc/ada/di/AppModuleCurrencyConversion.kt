package at.fhooe.mc.ada.di

import at.fhooe.mc.ada.features.feature_currencyConversion.data.repository.DefaultMainRepository
import at.fhooe.mc.ada.features.feature_currencyConversion.data.repository.MainRepository
import at.fhooe.mc.ada.features.feature_currencyConversion.data.CurrencyApi
import at.fhooe.mc.ada.features.feature_currencyConversion.data.Constants.Companion.BASE_URL
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModuleCurrencyConversion {

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}