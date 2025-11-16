package ir.amirroid.canvas.ui.mapper.element

import androidx.compose.ui.geometry.Size
import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.ui.components.paint.CanvasPathElement
import ir.amirroid.canvas.ui.mapper.path.toCompactDomainPath

fun CanvasPathElement.toDocumentDomainElement(boardSize: Size) = CanvasDocument.CanvasElement(
    type = type,
    path = toCompactDomainPath(boardSize),
    strokeWidth = strokeWidth
)