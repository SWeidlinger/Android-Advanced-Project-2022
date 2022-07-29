package at.fhooe.mc.ada.features.feature_card.presentation.util

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object MultiFab {
    var multiFabState = mutableStateOf(State.COLLAPSED)

    enum class State {
        COLLAPSED, EXPANDED
    }

    fun collapse() {
        multiFabState.value = State.COLLAPSED
    }

    fun expand() {
        multiFabState.value = State.EXPANDED
    }

    class Item(
        val icon: ImageVector,
        val color: Color,
        val identifier: String,
        val description: String,
        val label: String,
        val onClick: () -> Unit
    )
}

