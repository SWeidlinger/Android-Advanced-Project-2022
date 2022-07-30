package at.fhooe.mc.ada.features.feature_currency_converter.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fhooe.mc.ada.features.feature_currency_converter.data.CurrencyAndCountry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerListItem(item: CurrencyAndCountry, onCurrencyPickerListItemClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = onCurrencyPickerListItemClick,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.padding(10.dp, 5.dp, 0.dp, 5.dp)) {
                Text(
                    text = item.currencyCode,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )
                Text(text = item.countryName, fontSize = 12.sp, textAlign = TextAlign.Left)
            }
            Text(
                text = item.currencySymbol,
                fontSize = 25.sp,
                modifier = Modifier.padding(0.dp, 0.dp, 15.dp, 0.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}