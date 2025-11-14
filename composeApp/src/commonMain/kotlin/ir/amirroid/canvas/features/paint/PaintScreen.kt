package ir.amirroid.canvas.features.paint

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ir.amirroid.canvas.domain.models.PaintWithCanvasDocument
import ir.amirroid.canvas.utils.annotations.CommonParcelize

@CommonParcelize
data class PaintScreen(val paintId: Long) : Screen {
    sealed class State(
        val eventSink: (Event) -> Unit
    ) : CircuitUiState {
        data object Loading : State({})
        data class Success(
            val paintWithCanvasDocument: PaintWithCanvasDocument,
            private val eventSinkI: (Event) -> Unit
        ) : State(eventSinkI)
    }

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
    }
}