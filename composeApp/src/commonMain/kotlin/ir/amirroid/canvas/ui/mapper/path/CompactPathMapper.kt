package ir.amirroid.canvas.ui.mapper.path

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.domain.models.CanvasType


fun CanvasDocument.CanvasElement.CompactPath.toComposePath(
    type: CanvasType,
    boardSize: Size
): Path {
    return when (type) {
        CanvasType.PATH, CanvasType.CLEAR -> decodeToComposePath(boardSize)
        CanvasType.LINE -> {
            Path().apply {
                toBoundingRect()?.let { bounds ->
                    moveTo(bounds.topLeft.x, bounds.topLeft.y)
                    lineTo(bounds.bottomRight.x, bounds.bottomRight.y)
                    close()
                }
            }
        }

        CanvasType.OVAL -> {
            Path().apply {
                toBoundingRect()?.let {
                    addOval(it)
                }
            }
        }

        CanvasType.RECT -> {
            Path().apply {
                toBoundingRect()?.let {
                    addRect(it)
                }
            }
        }
    }
}

private fun CanvasDocument.CanvasElement.CompactPath.toBoundingRect(): Rect? {
    return coordinates.firstOrNull()?.let { start ->
        coordinates.lastOrNull()?.let { end ->
            Rect(
                topLeft = Offset(start.x, start.y),
                bottomRight = Offset(end.x, end.y)
            )
        }
    }
}

private fun CanvasDocument.CanvasElement.CompactPath.decodeToComposePath(boardSize: Size): Path {
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