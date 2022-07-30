package at.fhooe.mc.ada.features.feature_currency_converter.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fhooe.mc.ada.features.feature_currency_converter.data.CurrencyAndCountry
import at.fhooe.mc.ada.features.feature_currency_converter.domain.CurrencyWithRate
import kotlin.math.round

@Composable
fun CurrencyListItem(
    item: CurrencyAndCountry,
    currencyCountryList: List<CurrencyWithRate>,
    amountFromTextField: Double
) {
    var amount = 0.00
    val currentCountryItem = currencyCountryList.find {
        it.currencyCode == item.currencyCode
    }

    if (currentCountryItem != null) {
        amount = round(amountFromTextField * currentCountryItem.rate * 100) / 100
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = item.currencySymbol,
            fontSize = 20.sp,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 15.dp, 0.dp)
        )
        Text(
            text = if (amount == 0.00) "-" else amount.toString(),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold, modifier = Modifier
                .fillMaxSize()
        )
        Column(
            Modifier
                .padding(10.dp, 5.dp, 0.dp, 5.dp)
                .fillMaxSize(), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.currencyCode,
                fontSize = 20.sp,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = item.countryName,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 10.sp,
                textAlign = TextAlign.Left,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}