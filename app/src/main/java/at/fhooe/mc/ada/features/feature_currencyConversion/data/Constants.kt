package at.fhooe.mc.ada.features.feature_currencyConversion.data

import at.fhooe.mc.ada.features.feature_currencyConversion.data.CurrencyAndCountry

class Constants {
    companion object {
        const val BASE_URL = "https://api.exchangerate.host"

        val CURRENCY_CODES_LIST = listOf(
            CurrencyAndCountry("Australia", "AUD", "A\$", null),
            CurrencyAndCountry("Brazil", "BRL", "R\$", null),
            CurrencyAndCountry("Bulgaria", "BGN", "лв", null),
            CurrencyAndCountry("Canada", "CAD", "CA\$", null),
            CurrencyAndCountry("China", "CNY", "¥", null),
            CurrencyAndCountry("Croatia", "HRK", "kn", null),
            CurrencyAndCountry("Czech Republic", "CZK", "Kč", null),
            CurrencyAndCountry("Denmark", "DKK", "kr", null),
            CurrencyAndCountry("European Union", "EUR", "€", null),
            CurrencyAndCountry("Great Britain", "GBP", "£", null),
            CurrencyAndCountry("Hong Kong", "HKD", "HK\$", null),
            CurrencyAndCountry("Hungary", "HUF", "ft", null),
            CurrencyAndCountry("Iceland", "ISK", "Íkr", null),
            CurrencyAndCountry("India", "INR", "₹", null),
            CurrencyAndCountry("Indonesia", "IDR", "Rp", null),
            CurrencyAndCountry("Israel", "ILS", "₪", null),
            CurrencyAndCountry("Japan", "JPY", "¥", null),
            CurrencyAndCountry("Korea", "KRW", "₩", null),
            CurrencyAndCountry("Malaysia", "MYR", "RM", null),
            CurrencyAndCountry("Mexico", "MXN", "\$", null),
            CurrencyAndCountry("New Zealand", "NZD", "\$", null),
            CurrencyAndCountry("Norway", "NOK", "kr", null),
            CurrencyAndCountry("Philippines", "PHP", "₱", null),
            CurrencyAndCountry("Poland", "PLN", "zł", null),
            CurrencyAndCountry("Romania", "RON", "lei", null),
            CurrencyAndCountry("Russia", "RUB", "₽", null),
            CurrencyAndCountry("Singapore", "SGD", "S\$", null),
            CurrencyAndCountry("South Africa", "ZAR", "R", null),
            CurrencyAndCountry("Sweden", "SEK", "kr", null),
            CurrencyAndCountry("Switzerland", "CHF", "CHF", null),
            CurrencyAndCountry("Thailand", "THB", "฿", null),
            CurrencyAndCountry("Turkey", "TRY", "₺", null),
            CurrencyAndCountry("United States", "USD", "\$", null),
        )
    }
}