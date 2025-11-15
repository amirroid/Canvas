package ir.amirroid.canvas.features.paint

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ir.amirroid.canvas.domain.models.CanvasType
import ir.amirroid.canvas.domain.models.PaintWithCanvasDocument
import ir.amirroid.canvas.ui.components.paint.MotionEvent
import ir.amirroid.canvas.ui.components.paint.PaintState
import ir.amirroid.canvas.utils.annotations.CommonParcelize

@CommonParcelize
data class PaintScreen(val paintId: Long) : Screen {
    sealed class State(
        val eventSink: (Event) -> Unit
    ) : CircuitUiState {
        data object Loading : State({})

        @Immutable
        data class Success(
            val paintWithCanvasDocument: PaintWithCanvasDocument,
            val paintState: PaintState,
            private val eventSinkI: (Event) -> Unit
        ) : State(eventSinkI)
    }

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data class SetCanvasType(val type: CanvasType) : Event
        data class HandleMotionEvent(val motionEvent: MotionEvent) : Event
        data class InitPaintBoard(val boardSize: Size) : Event
        data object UndoCanvas : Event
        data object RedoCanvas : Event
        data object ClearCanvas : Event
        data object SaveCanvasAndBack : Event
    }
}