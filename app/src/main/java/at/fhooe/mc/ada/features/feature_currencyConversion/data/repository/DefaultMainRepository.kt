package at.fhooe.mc.ada.features.feature_currencyConversion.data.repository

import at.fhooe.mc.ada.features.feature_currencyConversion.data.CurrencyApi
import at.fhooe.mc.ada.features.feature_currencyConversion.data.models.CurrencyResponse
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching rates")
        }
    }

}