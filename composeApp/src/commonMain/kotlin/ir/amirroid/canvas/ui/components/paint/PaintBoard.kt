package ir.amirroid.canvas.ui.components.paint

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun PaintBoard(
    state: PaintState,
    modifier: Modifier = Modifier
) {
    val lineColor = LocalContentColor.current

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures(onDragEnd = {
                state.handleMotionEvent(MotionEvent.Up)
            }, onDragStart = {
                state.handleMotionEvent(MotionEvent.Down(it))
            }) { change, _ ->
                state.handleMotionEvent(MotionEvent.Drag(change.position))
                change.consume()
            }
        }
    ) {
        clipRect {
            state.paths.forEach { path ->
                drawPath(
                    path,
                    lineColor,
                    style = Stroke(
                        state.strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
            state.currentPath?.let { path ->
                drawPath(
                    path,
                    lineColor,
                    style = Stroke(
                        state.strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}