package ir.amirroid.canvas.features.home

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ir.amirroid.canvas.domain.models.PaintWithCanvasDocument
import ir.amirroid.canvas.utils.annotations.CommonParcelize

@CommonParcelize
data object HomeScreen : Screen {
    @Immutable
    data class State(
        val paints: List<PaintWithCanvasDocument>,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object NavigateToAddNewPaint : Event
    }
}