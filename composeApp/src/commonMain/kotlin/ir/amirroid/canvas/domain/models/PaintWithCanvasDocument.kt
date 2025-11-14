package ir.amirroid.canvas.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class PaintWithCanvasDocument(
    val paint: PaintItem,
    val document: CanvasDocument
)