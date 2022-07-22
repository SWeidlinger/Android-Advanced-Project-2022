package at.fhooe.mc.ada.features.feature_currencyConversion.data.repository

import at.fhooe.mc.ada.features.feature_currencyConversion.data.models.CurrencyResponse
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.Resource

interface MainRepository {
    suspend fun getRates(base: String): Resource<CurrencyResponse>
}