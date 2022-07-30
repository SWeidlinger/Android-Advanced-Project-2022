package at.fhooe.mc.ada.core.presentation.multiFab

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import at.fhooe.mc.ada.R

@Composable
fun AddNewItemMultiFab(
    modifier: Modifier = Modifier,
    multiFabState: MultiFab.State,
    items: List<MultiFab.Item>,
    onMultiFabStateChange: (MultiFab.State) -> Unit
) {
    val transition = updateTransition(targetState = multiFabState, label = "transitionIcon")

    val fabScale by transition.animateFloat(
        label = "fabScale",
        transitionSpec = { tween(durationMillis = 15) }) {
        if (it == MultiFab.State.COLLAPSED) 0f else 1f
    }

    val rotate by transition.animateFloat(label = "transitionIcon") {
        if (it == MultiFab.State.EXPANDED) 45f else 0f
    }

    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 50) }) {
        if (it == MultiFab.State.EXPANDED) 1f else 0f
    }

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End) {
        val visible = remember { mutableStateOf(true) }
        var scale: State<Float>
        val fabScale by transition.animateFloat(label = "fabScale") {
            if (it == MultiFab.State.COLLAPSED) 0f else 1f
        }

        if (transition.currentState == MultiFab.State.EXPANDED) {
            visible.value = true
            items.forEachIndexed { index, item ->
                MultiFabMinFabItem(
                    item = item,
                    scale = fabScale
                )
                Spacer(modifier = Modifier.size(23.dp))
            }
        } else {
            visible.value = false
        }
        FloatingActionButton(
            onClick = {
                onMultiFabStateChange(
                    if (transition.currentState == MultiFab.State.EXPANDED) MultiFab.State.COLLAPSED else MultiFab.State.EXPANDED
                )
            }, modifier = modifier, containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add_new_item_wallet),
                modifier = Modifier.rotate(rotate)
            )
        }
    }
}