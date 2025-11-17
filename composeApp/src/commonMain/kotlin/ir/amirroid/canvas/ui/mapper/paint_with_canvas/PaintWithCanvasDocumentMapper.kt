package ir.amirroid.canvas.ui.mapper.paint_with_canvas

import ir.amirroid.canvas.domain.models.PaintWithCanvasDocument
import ir.amirroid.canvas.ui.models.CanvasLoadState
import ir.amirroid.canvas.ui.models.PaintWithCanvasUiModel

fun PaintWithCanvasDocument.toLoadingUiModel() = toUiModel(CanvasLoadState.Loading)
fun PaintWithCanvasDocument.toUiModel(canvas: CanvasLoadState) = PaintWithCanvasUiModel(
    paint = paint,
    canvasName = document.name,
    canvas = canvas,
    board = document.board
)