package at.fhooe.mc.ada.util

import at.fhooe.mc.ada.data.CurrencyAndCountry

class Constants {
    companion object {
        const val BASE_URL = "https://api.exchangerate.host"

        val CURRENCY_CODES_LIST = listOf(
            CurrencyAndCountry("Australia", "AUD", null),
            CurrencyAndCountry("Brazil", "BRL", null),
            CurrencyAndCountry("Bulgaria", "BGN", null),
            CurrencyAndCountry("Canada", "CAD", null),
            CurrencyAndCountry("China", "CNY", null),
            CurrencyAndCountry("Croatia", "HRK", null),
            CurrencyAndCountry("Czech Republic", "CZK", null),
            CurrencyAndCountry("Denmark", "DKK", null),
            CurrencyAndCountry("European Union", "EUR", null),
            CurrencyAndCountry("Great Britain", "GBP", null),
            CurrencyAndCountry("Hong Kong", "HKD", null),
            CurrencyAndCountry("Hungary", "HUF", null),
            CurrencyAndCountry("Iceland", "ISK", null),
            CurrencyAndCountry("India", "INR", null),
            CurrencyAndCountry("Indonesia", "IDR", null),
            CurrencyAndCountry("Israel", "ILS", null),
            CurrencyAndCountry("Japan", "JPY", null),
            CurrencyAndCountry("Korea", "KRW", null),
            CurrencyAndCountry("Malaysia", "MYR", null),
            CurrencyAndCountry("Mexico", "MXN", null),
            CurrencyAndCountry("New Zealand", "NZD", null),
            CurrencyAndCountry("Norway", "NOK", null),
            CurrencyAndCountry("Philippines", "PHP", null),
            CurrencyAndCountry("Poland", "PLN", null),
            CurrencyAndCountry("Romania", "RON", null),
            CurrencyAndCountry("Russia", "RUB", null),
            CurrencyAndCountry("Singapore", "SGD", null),
            CurrencyAndCountry("South Africa", "ZAR", null),
            CurrencyAndCountry("Sweden", "SEK", null),
            CurrencyAndCountry("Switzerland", "CHF", null),
            CurrencyAndCountry("Thailand", "THB", null),
            CurrencyAndCountry("Turkey", "TRY", null),
            CurrencyAndCountry("United States", "USD", null),
        )
    }
}