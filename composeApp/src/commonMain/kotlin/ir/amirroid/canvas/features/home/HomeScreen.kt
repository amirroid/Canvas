package ir.amirroid.canvas.features.home

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ir.amirroid.canvas.ui.models.PaintWithCanvasUiModel
import ir.amirroid.canvas.utils.annotations.CommonParcelize

@CommonParcelize
data object HomeScreen : Screen {
    @Immutable
    data class State(
        val paints: List<PaintWithCanvasUiModel>,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object NavigateToAddNewPaint : Event
        data class ClickPaint(val paintId: Long) : Event
        data class LoadCanvasElements(val paintId: Long, val boardSize: Size) : Event
    }
}