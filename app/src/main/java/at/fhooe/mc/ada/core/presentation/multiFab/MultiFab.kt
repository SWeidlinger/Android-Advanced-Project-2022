package at.fhooe.mc.ada.core.presentation.multiFab

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

//Object for holding the MultiFabStates and expanding/collapsing
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

