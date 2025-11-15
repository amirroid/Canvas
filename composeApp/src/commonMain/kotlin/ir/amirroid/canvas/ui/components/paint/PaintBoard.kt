package ir.amirroid.canvas.ui.components.paint

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import ir.amirroid.canvas.domain.models.CanvasType

@Composable
fun PaintBoard(
    state: PaintState,
    onMotionEvent: (MotionEvent) -> Unit,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true
) {
    val lineColor = LocalContentColor.current

    Canvas(
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .pointerInput(gesturesEnabled) {
                if (!gesturesEnabled) return@pointerInput
                detectDragGestures(onDragEnd = {
                    onMotionEvent(MotionEvent.Up)
                }, onDragStart = {
                    onMotionEvent(MotionEvent.Down(it))
                }) { change, _ ->
                    onMotionEvent(MotionEvent.Drag(change.position))
                    change.consume()
                }
            }
    ) {
        clipRect {
            state.elements.forEach { element ->
                drawElement(element, lineColor)
            }
            state.currentElement?.let { element ->
                drawElement(element, lineColor)
            }
        }
    }
}


private fun DrawScope.drawElement(element: CanvasPathElement, lineColor: Color) {
    drawPath(
        element.path,
        lineColor,
        style = Stroke(
            width = element.strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        ),
        blendMode = if (element.type == CanvasType.CLEAR) BlendMode.Clear else BlendMode.SrcOver
    )
}