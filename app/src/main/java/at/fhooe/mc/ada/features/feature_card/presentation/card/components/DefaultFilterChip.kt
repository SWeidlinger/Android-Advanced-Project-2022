package at.fhooe.mc.ada.features.feature_card.presentation.card.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.logging.Filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultFilterChip(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            modifier = modifier,
            selected = selected,
            onClick = onSelect,
            label = { Text(text = text) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}