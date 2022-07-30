package at.fhooe.mc.ada.features.feature_currency_converter.domain

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.fhooe.mc.ada.features.feature_currency_converter.data.models.Rates
import at.fhooe.mc.ada.features.feature_currency_converter.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String, val rates: SnapshotStateList<CurrencyWithRate>) :
            CurrencyEvent()

        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String,
        currencySymbol: String?
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when (val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value =
                    CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val currencyList = getListOfCurrency(rates)

                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$convertedCurrency $currencySymbol", currencyList
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.CAD
        "HKD" -> rates.HKD
        "ISK" -> rates.ISK
        "EUR" -> rates.EUR
        "PHP" -> rates.PHP
        "DKK" -> rates.DKK
        "HUF" -> rates.HUF
        "CZK" -> rates.CZK
        "AUD" -> rates.AUD
        "RON" -> rates.RON
        "SEK" -> rates.SEK
        "IDR" -> rates.IDR
        "INR" -> rates.INR
        "BRL" -> rates.BRL
        "RUB" -> rates.RUB
        "HRK" -> rates.HRK
        "JPY" -> rates.JPY
        "THB" -> rates.THB
        "CHF" -> rates.CHF
        "SGD" -> rates.SGD
        "PLN" -> rates.PLN
        "BGN" -> rates.BGN
        "CNY" -> rates.CNY
        "NOK" -> rates.NOK
        "NZD" -> rates.NZD
        "ZAR" -> rates.ZAR
        "USD" -> rates.USD
        "MXN" -> rates.MXN
        "ILS" -> rates.ILS
        "GBP" -> rates.GBP
        "KRW" -> rates.KRW
        "MYR" -> rates.MYR
        "TRY" -> rates.TRY
        else -> null
    }

    private fun getListOfCurrency(rates: Rates): SnapshotStateList<CurrencyWithRate> {
        return mutableStateListOf(
            CurrencyWithRate("CAD", rates.CAD),
            CurrencyWithRate("HKD", rates.HKD),
            CurrencyWithRate("ISK", rates.ISK),
            CurrencyWithRate("EUR", rates.EUR),
            CurrencyWithRate("PHP", rates.PHP),
            CurrencyWithRate("DKK", rates.DKK),
            CurrencyWithRate("HUF", rates.HUF),
            CurrencyWithRate("CZK", rates.CZK),
            CurrencyWithRate("AUD", rates.AUD),
            CurrencyWithRate("RON", rates.RON),
            CurrencyWithRate("SEK", rates.SEK),
            CurrencyWithRate("IDR", rates.IDR),
            CurrencyWithRate("INR", rates.INR),
            CurrencyWithRate("BRL", rates.BRL),
            CurrencyWithRate("RUB", rates.RUB),
            CurrencyWithRate("HRK", rates.HRK),
            CurrencyWithRate("JPY", rates.JPY),
            CurrencyWithRate("THB", rates.THB),
            CurrencyWithRate("CHF", rates.CHF),
            CurrencyWithRate("SGD", rates.SGD),
            CurrencyWithRate("PLN", rates.PLN),
            CurrencyWithRate("BGN", rates.BGN),
            CurrencyWithRate("CNY", rates.CNY),
            CurrencyWithRate("NOK", rates.NOK),
            CurrencyWithRate("NZD", rates.NZD),
            CurrencyWithRate("ZAR", rates.ZAR),
            CurrencyWithRate("USD", rates.USD),
            CurrencyWithRate("MXN", rates.MXN),
            CurrencyWithRate("ILS", rates.ILS),
            CurrencyWithRate("GBP", rates.GBP),
            CurrencyWithRate("KRW", rates.KRW),
            CurrencyWithRate("MYR", rates.MYR),
            CurrencyWithRate("TRY", rates.TRY)
        )
    }
}

class CurrencyWithRate(val currencyCode: String, val rate: Double)