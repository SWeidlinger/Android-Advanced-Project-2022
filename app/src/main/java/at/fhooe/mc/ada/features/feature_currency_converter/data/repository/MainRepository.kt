package at.fhooe.mc.ada.features.feature_currency_converter.data.repository

import at.fhooe.mc.ada.features.feature_currency_converter.data.models.CurrencyResponse
import at.fhooe.mc.ada.features.feature_currency_converter.domain.Resource

interface MainRepository {
    suspend fun getRates(base: String): Resource<CurrencyResponse>
}