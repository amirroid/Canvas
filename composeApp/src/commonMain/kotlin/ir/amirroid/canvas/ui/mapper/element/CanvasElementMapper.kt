package ir.amirroid.canvas.ui.mapper.element

import androidx.compose.ui.geometry.Size
import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.ui.components.paint.CanvasPathElement
import ir.amirroid.canvas.ui.mapper.path.toComposePath

fun CanvasDocument.CanvasElement.toCanvasUiElement(boardSize: Size) = CanvasPathElement(
    type = type,
    path = path.toComposePath(boardSize),
    strokeWidth = strokeWidth
)