package ir.amirroid.canvas.ui.models

import androidx.compose.runtime.Immutable
import ir.amirroid.canvas.domain.models.Board
import ir.amirroid.canvas.domain.models.PaintItem
import ir.amirroid.canvas.ui.components.paint.PaintState

@Immutable
data class PaintWithCanvasUiModel(
    val paint: PaintItem,
    val canvasName: String,
    val board: Board,
    val canvas: CanvasLoadState
)

sealed interface CanvasLoadState {
    data object Loading : CanvasLoadState

    @Immutable
    data class Ready(val paintState: PaintState) : CanvasLoadState
}