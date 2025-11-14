package ir.amirroid.canvas.ui.components.paint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.copy
import ir.amirroid.canvas.domain.models.CanvasType

sealed class MotionEvent(
    val x: Float,
    val y: Float,
) {
    data object Idle : MotionEvent(0f, 0f)
    data object Up : MotionEvent(0f, 0f)
    data class Down(val position: Offset) : MotionEvent(position.x, position.y)
    data class Drag(val position: Offset) : MotionEvent(position.x, position.y)

    fun asOffset() = Offset(x, y)
}

@Stable
class PaintState {
    var currentPath by mutableStateOf<Path?>(null)
    var paths = mutableStateListOf<Path>()

    var currentCanvasType by mutableStateOf(CanvasType.PATH)
    var previewsPosition: Offset? = null

    var strokeWidth by mutableFloatStateOf(20f)


    fun handleMotionEvent(event: MotionEvent) {
        when (event) {
            is MotionEvent.Idle -> Unit
            is MotionEvent.Down -> {
                previewsPosition = event.asOffset()
                currentPath = Path().apply {
                    moveTo(event.x, event.y)
                }
            }

            is MotionEvent.Up -> {
                if (currentPath == null) return
                paths += currentPath!!
            }

            is MotionEvent.Drag -> {
                if (previewsPosition == null) return

                handleDragEvent(event)
            }
        }
    }


    private fun handleDragEvent(event: MotionEvent.Drag) {
        when (currentCanvasType) {
            CanvasType.LINE -> {
                currentPath = Path().apply {
                    moveTo(previewsPosition!!.x, previewsPosition!!.y)
                    lineTo(event.x, event.y)
                }
            }

            CanvasType.OVAL -> {
                currentPath = Path().apply {
                    addOval(getEventRect(event))
                }
            }

            CanvasType.RECT -> {
                currentPath = Path().apply {
                    addRect(getEventRect(event))
                }
            }

            CanvasType.PATH -> {
                previewsPosition?.let {
                    currentPath = currentPath!!.copy().apply {
                        quadraticTo(
                            it.x,
                            it.y,
                            it.x.plus(event.x).div(2),
                            it.y.plus(event.y).div(2),
                        )
                    }
                    previewsPosition = event.asOffset()
                }
            }
        }
    }

    private fun getEventRect(event: MotionEvent) = Rect(previewsPosition!!, event.asOffset())
}


@Composable
fun rememberPaintState(): PaintState {
    return remember {
        PaintState()
    }
}