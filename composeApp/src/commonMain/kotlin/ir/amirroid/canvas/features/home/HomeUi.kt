package ir.amirroid.canvas.features.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import canvas.composeapp.generated.resources.Res
import canvas.composeapp.generated.resources.home
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import ir.amirroid.canvas.ui.components.paint.PaintBoard
import ir.amirroid.canvas.ui.models.CanvasLoadState
import ir.amirroid.canvas.ui.models.PaintWithCanvasUiModel
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(HomeScreen::class, AppScope::class)
class HomeUi : Ui<HomeScreen.State> {
    @Composable
    override fun Content(
        state: HomeScreen.State,
        modifier: Modifier
    ) {
        HomeContent(state, modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(state: HomeScreen.State, modifier: Modifier = Modifier) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    state.eventSink.invoke(HomeScreen.Event.NavigateToAddNewPaint)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(Res.string.home))
                }
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(state.paints, key = { it.paint.id }) { paintWthDocument ->
                    PaintItem(
                        paintWthDocument = paintWthDocument,
                        onClick = {
                            state.eventSink.invoke(HomeScreen.Event.ClickPaint(paintWthDocument.paint.id))
                        },
                        onLoadRequest = { size ->
                            state.eventSink.invoke(
                                HomeScreen.Event.LoadCanvasElements(
                                    paintWthDocument.paint.id,
                                    size
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun PaintItem(
    paintWthDocument: PaintWithCanvasUiModel,
    onClick: () -> Unit,
    onLoadRequest: (boardSize: Size) -> Unit,
) {
    var boardSize by remember {
        mutableStateOf(Size.Zero)
    }
    val canvas = paintWthDocument.canvas

    LaunchedEffect(boardSize, canvas) {
        if (canvas is CanvasLoadState.Loading && boardSize != Size.Zero) {
            onLoadRequest.invoke(boardSize)
        }
    }

    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(paintWthDocument.board.aspectRatio)
                    .onSizeChanged {
                        boardSize = it.toSize()
                    },
                shape = MaterialTheme.shapes.small
            ) {
                AnimatedContent(canvas) { currentCanvas ->
                    when (currentCanvas) {
                        is CanvasLoadState.Loading -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }

                        is CanvasLoadState.Ready -> {
                            PaintBoard(
                                state = currentCanvas.paintState,
                                onMotionEvent = {},
                                gesturesEnabled = false
                            )
                        }
                    }
                }
            }
            Text(paintWthDocument.canvasName, modifier = Modifier.padding(top = 8.dp))
        }
    }
}