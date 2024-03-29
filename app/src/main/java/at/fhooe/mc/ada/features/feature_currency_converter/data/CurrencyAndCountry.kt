package at.fhooe.mc.ada.features.feature_currency_converter.data

import androidx.compose.ui.graphics.painter.Painter

data class CurrencyAndCountry(
    val countryName: String,
    val currencyCode: String,
    val currencySymbol: String,
    val painter: Painter?
)
