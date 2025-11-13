package ir.amirroid.canvas.features.home.circuit

import androidx.compose.runtime.Immutable
import cl.emilym.kmp.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

@Parcelize
data object HomeScreen : Screen {
    @Immutable
    data class State(
        val name: String
    ) : CircuitUiState
}