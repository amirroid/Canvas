package ir.amirroid.canvas.features.paint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ir.amirroid.canvas.domain.models.Board
import ir.amirroid.canvas.domain.models.PaintWithCanvasDocument
import ir.amirroid.canvas.domain.usecase.GetPaintWithCanvasDocumentUseCase
import ir.amirroid.canvas.domain.usecase.SaveCanvasDocumentUseCase
import ir.amirroid.canvas.effects.RetainedLaunchedEffect
import ir.amirroid.canvas.ui.components.paint.rememberRetainedPaintState
import ir.amirroid.canvas.ui.mapper.element.toCanvasUiElement
import ir.amirroid.canvas.ui.mapper.element.toDocumentDomainElement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(PaintScreen::class, AppScope::class)
class PaintPresenter @Inject constructor(
    private val getPaintWithCanvasDocumentUseCase: GetPaintWithCanvasDocumentUseCase,
    saveCanvasDocumentUseCase: Lazy<SaveCanvasDocumentUseCase>,
    private val dispatcher: CoroutineDispatcher,
    @Assisted private val paintScreen: PaintScreen,
    @Assisted private val navigator: Navigator
) : Presenter<PaintScreen.State> {
    private val saveCanvasDocumentUseCase by saveCanvasDocumentUseCase

    @Composable
    override fun present(): PaintScreen.State {
        val scope = rememberCoroutineScope()
        var currentPaint by rememberRetained {
            mutableStateOf<Result<PaintWithCanvasDocument>?>(null)
        }
        val paintState = rememberRetainedPaintState()

        fun saveCanvasAndBack() {
            currentPaint?.getOrNull()?.let { (paint, document) ->
                scope.launch(dispatcher) {
                    saveCanvasDocumentUseCase.invoke(
                        paint.fileUri,
                        document.copy(
                            elements = paintState.elements.map {
                                it.toDocumentDomainElement(
                                    paintState.boardSize
                                )
                            },
                            board = Board(paintState.boardSize.width, paintState.boardSize.height)
                        )
                    )
                    withContext(Dispatchers.Main) {
                        navigator.pop(
                            PaintScreen.Result(
                                isEdited = true,
                                paintId = paintScreen.paintId
                            )
                        )
                    }
                }
            }
        }

        LaunchedEffect(paintScreen.paintId) {
            if (currentPaint?.isSuccess == true) return@LaunchedEffect

            withContext(dispatcher) {
                currentPaint = runCatching {
                    getPaintWithCanvasDocumentUseCase.invoke(paintId = paintScreen.paintId)!!
                }
            }
        }

        RetainedLaunchedEffect(paintState.isInitialized, currentPaint) {
            if (paintState.isInitialized.not()) return@RetainedLaunchedEffect

            currentPaint?.onSuccess { (_, document) ->
                paintState.initializeElements(
                    newElements = document.elements.map { element ->
                        element.toCanvasUiElement(paintState.boardSize)
                    }
                )
            }
        }

        val eventSink: (PaintScreen.Event) -> Unit = { event ->
            when (event) {
                is PaintScreen.Event.Back -> navigator.pop(
                    PaintScreen.Result(
                        isEdited = false,
                        paintId = paintScreen.paintId
                    )
                )

                is PaintScreen.Event.RedoCanvas -> paintState.redo()
                is PaintScreen.Event.UndoCanvas -> paintState.undo()
                is PaintScreen.Event.ClearCanvas -> paintState.clearAll()
                is PaintScreen.Event.SetCanvasType -> paintState.currentCanvasType = event.type
                is PaintScreen.Event.HandleMotionEvent -> paintState.handleMotionEvent(event.motionEvent)
                is PaintScreen.Event.InitPaintBoard -> paintState.initializeBoard(event.boardSize)
                is PaintScreen.Event.SaveCanvasAndBack -> saveCanvasAndBack()
            }
        }

        return when {
            currentPaint?.isSuccess == true -> {
                PaintScreen.State.Success(
                    paintWithCanvasDocument = currentPaint!!.getOrThrow(),
                    paintState = paintState,
                    eventSinkI = eventSink
                )
            }

            else -> PaintScreen.State.Loading
        }
    }
}