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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyCard(
    currencyCode: String,
    label: String,
    modifier: Modifier,
    onCurrencyCardClick: () -> Unit
) {
    Card(
        modifier = modifier.offset(0.dp, 1.dp),
        onClick = onCurrencyCardClick,
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(start = 10.dp, top = 7.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = label,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start
                    )
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = currencyCode,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

        })
}