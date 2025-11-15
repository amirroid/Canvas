package ir.amirroid.canvas.features.paint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ir.amirroid.canvas.domain.models.PaintWithCanvasDocument
import ir.amirroid.canvas.domain.usecase.GetPaintWithCanvasDocumentUseCase
import ir.amirroid.canvas.ui.components.paint.rememberRetainedPaintState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(PaintScreen::class, AppScope::class)
class PaintPresenter @Inject constructor(
    private val getPaintWithCanvasDocumentUseCase: GetPaintWithCanvasDocumentUseCase,
    @Assisted private val paintScreen: PaintScreen,
    @Assisted private val navigator: Navigator
) : Presenter<PaintScreen.State> {
    @Composable
    override fun present(): PaintScreen.State {
        var currentPaint by rememberRetained {
            mutableStateOf<Result<PaintWithCanvasDocument>?>(null)
        }
        val paintState = rememberRetainedPaintState()

        LaunchedEffect(Unit) {
            if (currentPaint?.isSuccess == true) return@LaunchedEffect

            withContext(Dispatchers.IO) {
                currentPaint = runCatching {
                    getPaintWithCanvasDocumentUseCase.invoke(paintId = paintScreen.paintId)!!
                }
            }
        }

        val eventSink: (PaintScreen.Event) -> Unit = { event ->
            when (event) {
                is PaintScreen.Event.Back -> navigator.pop()
                is PaintScreen.Event.RedoCanvas -> paintState.redo()
                is PaintScreen.Event.UndoCanvas -> paintState.undo()
                is PaintScreen.Event.ClearCanvas -> paintState.clearAll()
                is PaintScreen.Event.SetCanvasType -> paintState.currentCanvasType = event.type
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