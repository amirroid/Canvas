package ir.amirroid.canvas.ui.mapper.path

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import ir.amirroid.canvas.domain.models.CanvasDocument
import kotlin.math.min

fun Path.toCompactDomainPath(
    boardSize: Size,
    maxSampling: Int = 500
): CanvasDocument.CanvasElement.CompactPath {

    val coords = mutableListOf<CanvasDocument.CanvasElement.CompactPath.Coordinate>()

    val measure = PathMeasure().apply {
        setPath(this@toCompactDomainPath, false)
    }
    val length = measure.length

    if (length == 0f) {
        return CanvasDocument.CanvasElement.CompactPath(emptyList())
    }

    val desiredStep = 4f
    val realSampling = min(maxSampling, (length / desiredStep).toInt().coerceAtLeast(1))

    val step = length / realSampling

    for (i in 0 until realSampling) {
        val distance = i * step
        val pos = measure.getPosition(distance)

        coords += CanvasDocument.CanvasElement.CompactPath.Coordinate(
            x = pos.x / boardSize.width,
            y = pos.y / boardSize.height
        )
    }

    return CanvasDocument.CanvasElement.CompactPath(coords)
}