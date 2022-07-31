package at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardStyleCard(modifier: Modifier, color: Color, checked: Boolean, onCardClick: () -> Unit) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(color),
        onClick = onCardClick,
        border = if (checked) BorderStroke(2.dp, color = MaterialTheme.colorScheme.onBackground) else null,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Checked Card",
                    modifier = Modifier.padding(5.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}