package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fhooe.mc.ada.ui.theme.NegativeRed
import at.fhooe.mc.ada.ui.theme.PositiveGreen
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItemBudgetTracker(
    modifier: Modifier,
    index: Int,
    descriptionText: String,
    date: String,
    amount: String,
    isExpense: Boolean
) {
    Card(
        modifier = modifier,
        shape = if (index == 0) RoundedCornerShape(
            topEnd = 10.dp,
            topStart = 10.dp
        ) else RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = descriptionText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = date, fontSize = 14.sp)
            }
            var finalAmount = 0.0
            if (amount.isNotEmpty()) {
                finalAmount = round(amount.toDouble() * 100) / 100
                if (isExpense) finalAmount *= -1
            }
            var finalAmountString = if (finalAmount < 0) "$finalAmount" else "+$finalAmount"
            var color = if (isExpense) NegativeRed else PositiveGreen
            Text(
                text = if (finalAmount == 0.0) "0.0" else finalAmountString,
                color = if (finalAmount == 0.0) MaterialTheme.colorScheme.onBackground else color,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Right
            )
        }
    }
}