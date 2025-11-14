package ir.amirroid.canvas.features.add_paint

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ir.amirroid.canvas.utils.annotations.CommonParcelize

@CommonParcelize
data object AddNewPaintScreen : Screen {
    @Immutable
    data class State(
        val name: String
    ) : CircuitUiState
}