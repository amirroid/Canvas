package ir.amirroid.canvas.features.paint

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Rectangle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Draw
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import canvas.composeapp.generated.resources.Res
import canvas.composeapp.generated.resources.discard
import canvas.composeapp.generated.resources.save
import canvas.composeapp.generated.resources.save_changes_title
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuitx.overlays.DialogResult
import com.slack.circuitx.overlays.alertDialogOverlay
import ir.amirroid.canvas.domain.models.CanvasType
import ir.amirroid.canvas.ui.components.BackHandler
import ir.amirroid.canvas.ui.components.paint.PaintBoard
import ir.amirroid.canvas.ui.icons.LineIcon
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Immutable
data class CanvasTypeWithIcon(
    val type: CanvasType,
    val icon: ImageVector
)

val canvasTypesWithIcons = listOf(
    CanvasTypeWithIcon(
        type = CanvasType.PATH,
        icon = Icons.Rounded.Draw
    ),
    CanvasTypeWithIcon(
        type = CanvasType.LINE,
        icon = LineIcon
    ),
    CanvasTypeWithIcon(
        type = CanvasType.RECT,
        icon = Icons.Outlined.Rectangle
    ),
    CanvasTypeWithIcon(
        type = CanvasType.OVAL,
        icon = Icons.Outlined.Circle
    ),
    CanvasTypeWithIcon(
        type = CanvasType.CLEAR,
        icon = Icons.Outlined.CleaningServices
    ),
)

@CircuitInject(PaintScreen::class, AppScope::class)
class PaintUi : Ui<PaintScreen.State> {
    @Composable
    override fun Content(
        state: PaintScreen.State,
        modifier: Modifier
    ) {
        ContentWithOverlays {
            if (state is PaintScreen.State.Success) {
                PaintContent(state, modifier)
            }
            if (state is PaintScreen.State.Loading || (state is PaintScreen.State.Success && state.paintState.isInitialized.not())) {
                PaintLoadingContent()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PaintContent(state: PaintScreen.State.Success, modifier: Modifier = Modifier) {
    val paintState = state.paintState
    val eventSink = state.eventSink


    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()


    fun back() {
        if (paintState.undoList.isNotEmpty()) {
            scope.launch {
                val result = overlayHost.showSaveDiscardDialog()
                if (result == DialogResult.Confirm) {
                    eventSink.invoke(PaintScreen.Event.SaveCanvasAndBack)
                } else {
                    state.eventSink.invoke(PaintScreen.Event.Back)
                }
            }
        } else state.eventSink.invoke(PaintScreen.Event.Back)
    }

    BackHandler { back() }


    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            title = {
                Text(state.paintWithCanvasDocument.document.name)
            },
            navigationIcon = {
                IconButton(onClick = ::back) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    eventSink.invoke(PaintScreen.Event.SaveCanvasAndBack)
                }, enabled = paintState.undoList.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = null
                    )
                }
            }
        )
        Card(modifier = Modifier.weight(1f).padding(12.dp)) {
            PaintBoard(
                state = paintState,
                onMotionEvent = { state.eventSink.invoke(PaintScreen.Event.HandleMotionEvent(it)) },
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged {
                        eventSink.invoke(PaintScreen.Event.InitPaintBoard(it.toSize()))
                    }
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .padding(bottom = 12.dp)
                .padding(horizontal = 12.dp)
                .navigationBarsPadding(),
            shape = CircleShape
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                canvasTypesWithIcons.forEach { (type, icon) ->
                    SectionIconButton(
                        icon = icon,
                        onClick = {
                            eventSink.invoke(PaintScreen.Event.SetCanvasType(type))
                        },
                        selected = paintState.currentCanvasType == type
                    )
                }
                SectionIconButton(
                    icon = Icons.AutoMirrored.Rounded.Undo,
                    onClick = { eventSink.invoke(PaintScreen.Event.UndoCanvas) },
                    enabled = paintState.undoList.isNotEmpty()
                )
                SectionIconButton(
                    icon = Icons.AutoMirrored.Rounded.Redo,
                    onClick = { eventSink.invoke(PaintScreen.Event.RedoCanvas) },
                    enabled = paintState.redoList.isNotEmpty()
                )
                SectionIconButton(
                    icon = Icons.Rounded.Delete,
                    onClick = { eventSink.invoke(PaintScreen.Event.ClearCanvas) },
                    enabled = paintState.elements.isNotEmpty()
                )
            }
        }
    }
}

@Composable
fun RowScope.SectionIconButton(
    icon: ImageVector,
    selected: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(enabled = enabled) {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.alpha(if (!selected || !enabled) .6f else 1f)
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PaintLoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LoadingIndicator(modifier = Modifier.size(88.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
suspend fun OverlayHost.showSaveDiscardDialog(): DialogResult {
    return show(
        alertDialogOverlay(
            title = { Text(stringResource(Res.string.save_changes_title)) },
            confirmButton = { onClick ->
                Button(onClick = onClick) { Text(stringResource(Res.string.save)) }
            },
            dismissButton = { onClick ->
                TextButton(onClick = onClick) { Text(stringResource(Res.string.discard)) }
            },
        )
    )
}