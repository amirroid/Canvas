package ir.amirroid.canvas.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.domain.usecase.GetAllPaintWithCanvasesUseCase
import ir.amirroid.canvas.domain.usecase.GetPaintWithCanvasDocumentUseCase
import ir.amirroid.canvas.features.add_paint.AddNewPaintScreen
import ir.amirroid.canvas.features.paint.PaintScreen
import ir.amirroid.canvas.ui.components.paint.PaintState
import ir.amirroid.canvas.ui.mapper.element.toCanvasUiElement
import ir.amirroid.canvas.ui.mapper.paint_with_canvas.toLoadingUiModel
import ir.amirroid.canvas.ui.models.CanvasLoadState
import ir.amirroid.canvas.ui.models.PaintWithCanvasUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(HomeScreen::class, AppScope::class)
class HomePresenter @Inject constructor(
    private val getAllPaintWithCanvasesUseCase: GetAllPaintWithCanvasesUseCase,
    private val getPaintWithCanvasDocumentUseCase: GetPaintWithCanvasDocumentUseCase,
    private val dispatcher: CoroutineDispatcher,
    @Assisted private val navigator: Navigator
) : Presenter<HomeScreen.State> {
    @Composable
    override fun present(): HomeScreen.State {
        var paints by rememberRetained {
            mutableStateOf(emptyList<PaintWithCanvasUiModel>())
        }
        val documents = rememberRetained {
            mutableMapOf<Long, CanvasDocument>()
        }

        val scope = rememberCoroutineScope()

        val paintNavigator = rememberAnsweringNavigator<PaintScreen.Result>(navigator) { result ->
            if (result.isEdited) {
                scope.launch(dispatcher) {
                    getPaintWithCanvasDocumentUseCase.invoke(result.paintId)
                        ?.let { (paint, document) ->
                            documents[paint.id] = document
                            paints = paints.map { paintWithCanvas ->
                                if (paintWithCanvas.paint.id == paint.id) {
                                    paintWithCanvas.copy(
                                        canvas = CanvasLoadState.Loading,
                                        board = document.board
                                    )
                                } else paintWithCanvas
                            }
                        }
                }
            }
        }

        LaunchedEffect(Unit) {
            getAllPaintWithCanvasesUseCase()
                .collectLatest { newPaints ->
                    paints = newPaints.map { item ->
                        documents[item.paint.id] = item.document

                        val previewsItem =
                            paints.find { item.paint.id == it.paint.id }
                                ?: return@map item.toLoadingUiModel()
                        when (previewsItem.canvas) {
                            is CanvasLoadState.Loading -> item.toLoadingUiModel()
                            is CanvasLoadState.Ready -> {
//                                val boardSize = previewsItem.canvas.paintState.boardSize
//                                item.toUiModel(canvas = getReadyCanvas(boardSize, item.document))
                                previewsItem
                            }
                        }
                    }
                }
        }

        return HomeScreen.State(paints = paints) { event ->
            when (event) {
                is HomeScreen.Event.NavigateToAddNewPaint -> {
                    navigator.goTo(AddNewPaintScreen)
                }

                is HomeScreen.Event.ClickPaint -> {
                    paintNavigator.goTo(PaintScreen(event.paintId))
                }

                is HomeScreen.Event.LoadCanvasElements -> {
                    scope.launch {
                        paints = paints.map { item ->
                            updateCanvasIfNeeded(
                                item = item,
                                paintId = event.paintId,
                                boardSize = event.boardSize,
                                documents = documents,
                                getReadyCanvas = ::getReadyCanvas
                            )
                        }
                    }
                }
            }
        }
    }


    private fun getReadyCanvas(boardSize: Size, document: CanvasDocument): CanvasLoadState.Ready {
        val newPaintState = PaintState().apply {
            initializeBoard(boardSize)
            initializeElements(document.elements.map {
                it.toCanvasUiElement(
                    boardSize
                )
            })
        }
        return CanvasLoadState.Ready(newPaintState)
    }

    private fun updateCanvasIfNeeded(
        paintId: Long,
        boardSize: Size,
        item: PaintWithCanvasUiModel,
        documents: Map<Long, CanvasDocument>,
        getReadyCanvas: (Size, CanvasDocument) -> CanvasLoadState.Ready
    ): PaintWithCanvasUiModel {

        if (item.paint.id != paintId) return item
        if (item.canvas !is CanvasLoadState.Loading) return item

        val document = documents[item.paint.id] ?: return item

        return item.copy(
            canvas = getReadyCanvas(boardSize, document)
        )
    }
}