package ir.amirroid.canvas.ui.components.paint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.copy
import com.slack.circuit.retained.rememberRetained
import ir.amirroid.canvas.domain.models.CanvasType
import kotlin.math.max
import kotlin.math.min

enum class UndoRedoAction {
    ADD_ELEMENT,
    REMOVE_ELEMENT
}

@Immutable
data class UndoRedoEntry(
    val action: UndoRedoAction,
    val elements: List<CanvasPathElement>
)

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
    var currentElement by mutableStateOf<CanvasPathElement?>(null)
        private set
    var elements = mutableStateListOf<CanvasPathElement>()
        private set

    var undoList = mutableStateListOf<UndoRedoEntry>()
        private set
    var redoList = mutableStateListOf<UndoRedoEntry>()
        private set

    var currentCanvasType by mutableStateOf(CanvasType.PATH)
    var previewsPosition: Offset? = null

    var isInitialized by mutableStateOf(false)
    private var boardSize = Size.Zero


    fun initialize(boardSize: Size) {
        this.boardSize = boardSize
        isInitialized = true
    }

    fun redo() {
        val lastItem = redoList.removeLastOrNull() ?: return
        if (lastItem.action == UndoRedoAction.ADD_ELEMENT) {
            elements.addAll(lastItem.elements)
        } else {
            elements.removeAll(lastItem.elements)
        }
        undoList += lastItem
    }

    fun undo() {
        val lastItem = undoList.removeLastOrNull() ?: return
        if (lastItem.action == UndoRedoAction.ADD_ELEMENT) {
            elements.removeAll(lastItem.elements)
        } else {
            elements.addAll(lastItem.elements)
        }
        redoList += lastItem
    }

    fun clearAll() {
        undoList += UndoRedoEntry(
            action = UndoRedoAction.REMOVE_ELEMENT,
            elements = elements.toList()
        )
        elements.clear()
    }

    fun handleMotionEvent(event: MotionEvent) {
        when (event) {
            is MotionEvent.Idle -> Unit
            is MotionEvent.Down -> {
                previewsPosition = event.asOffset()
                currentElement = Path().apply {
                    moveTo(event.x, event.y)
                }.createElement()
            }

            is MotionEvent.Up -> {
                if (currentElement == null) return
                elements += currentElement!!
                undoList += UndoRedoEntry(
                    action = UndoRedoAction.ADD_ELEMENT,
                    elements = listOf(currentElement!!)
                )
                currentElement = null
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
                currentElement = Path().apply {
                    moveTo(previewsPosition!!.x, previewsPosition!!.y)
                    lineTo(event.x, event.y)
                }.createElement()
            }

            CanvasType.OVAL -> {
                currentElement = Path().apply {
                    addOval(getEventRect(event))
                }.createElement()
            }

            CanvasType.RECT -> {
                currentElement = Path().apply {
                    addRect(getEventRect(event))
                }.createElement()
            }

            CanvasType.PATH, CanvasType.CLEAR -> {
                previewsPosition?.let { offset ->
                    currentElement = currentElement?.let { element ->
                        element.copy(
                            path = element.path.copy().apply {
                                quadraticTo(
                                    offset.x,
                                    offset.y,
                                    offset.x.plus(event.x).div(2),
                                    offset.y.plus(event.y).div(2),
                                )
                            }
                        )
                    }
                    previewsPosition = event.asOffset()
                }
            }
        }
    }

    private fun Path.createElement() = CanvasPathElement(
        type = currentCanvasType,
        path = this
    )

    private fun getEventRect(event: MotionEvent): Rect {
        val p1 = previewsPosition!!
        val p2 = event.asOffset()

        return Rect(
            Offset(min(p1.x, p2.x), min(p1.y, p2.y)),
            Offset(max(p1.x, p2.x), max(p1.y, p2.y))
        )
    }
}


@Composable
fun rememberPaintState(): PaintState {
    return remember {
        PaintState()
    }
}


@Composable
fun rememberRetainedPaintState(): PaintState {
    return rememberRetained {
        PaintState()
    }
}