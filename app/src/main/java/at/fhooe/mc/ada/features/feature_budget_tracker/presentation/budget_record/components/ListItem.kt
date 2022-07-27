package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItemBudgetTracker(
    modifier: Modifier,
    descriptionText: String,
    date: String,
    amount: Double,
    onItemClick: () -> Unit
) {
    Card(modifier = modifier, onClick = onItemClick) {
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
            Text(
                text = if (amount < 0) "$amount" else "+$amount",
                color = if (amount < 0) Color.Red else Color.Green,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Right
            )
        }
    }
}