package ir.amirroid.canvas.features.home.circuit

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ir.amirroid.canvas.utils.annotations.CommonParcelize

@CommonParcelize
data object HomeScreen : Screen {
    @Immutable
    data class State(
        val name: String
    ) : CircuitUiState
}