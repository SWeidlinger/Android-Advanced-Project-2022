package at.fhooe.mc.ada.currencyConversion

import at.fhooe.mc.ada.data.models.CurrencyResponse
import at.fhooe.mc.ada.util.Resource

interface MainRepository {
    suspend fun getRates(base: String): Resource<CurrencyResponse>
}