package at.fhooe.mc.ada.core.presentation.multiFab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import at.fhooe.mc.ada.ui.theme.TextWhite

@Composable
fun MultiFabMinFabItem(
    item: MultiFab.Item,
    scale: Float
) {
    Row(
        modifier = Modifier.clickable {
            item.onClick.invoke()
        }, verticalAlignment = Alignment.CenterVertically
    ) {
        var scaleText = 1f
        if (scale < 1) {
            scaleText = 0f
        }
        Text(
            text = item.label,
            modifier = Modifier.scale(scaleText),
            textAlign = TextAlign.End,
            color = TextWhite
        )
        Spacer(modifier = Modifier.padding(15.dp))
        SmallFloatingActionButton(
            onClick = {
                item.onClick.invoke()
            },
            Modifier
                .size(35.dp)
                .scale(scale)
                .offset((-10).dp, 0.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp), containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.description
            )
        }
    }
}