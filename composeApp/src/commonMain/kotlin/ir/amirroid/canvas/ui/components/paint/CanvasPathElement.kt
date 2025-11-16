package ir.amirroid.canvas.ui.components.paint

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import ir.amirroid.canvas.domain.models.CanvasType

@Immutable
data class CanvasPathElement(
    val type: CanvasType,
    val path: Path,
    val strokeWidth: Float = 20f,
    // Not used for PATH type
    val points: List<Offset> = emptyList()
)