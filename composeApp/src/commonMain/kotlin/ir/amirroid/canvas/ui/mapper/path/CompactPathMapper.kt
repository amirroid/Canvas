package ir.amirroid.canvas.ui.mapper.path

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import ir.amirroid.canvas.domain.models.CanvasDocument


fun CanvasDocument.CanvasElement.CompactPath.toComposePath(boardSize: Size): Path {
    val path = Path()
    if (coordinates.isEmpty()) return path

    val start = coordinates.first()
    path.moveTo(
        start.x * boardSize.width,
        start.y * boardSize.height
    )

    for (i in 1 until coordinates.size) {
        val p = coordinates[i]
        path.lineTo(
            p.x * boardSize.width,
            p.y * boardSize.height
        )
    }

    return path
}