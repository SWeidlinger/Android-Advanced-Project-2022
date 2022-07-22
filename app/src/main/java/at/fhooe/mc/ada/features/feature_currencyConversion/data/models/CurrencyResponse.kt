package at.fhooe.mc.ada.features.feature_currencyConversion.data.models

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean
)