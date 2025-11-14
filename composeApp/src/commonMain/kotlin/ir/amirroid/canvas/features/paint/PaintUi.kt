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
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import ir.amirroid.canvas.domain.models.CanvasType
import ir.amirroid.canvas.ui.components.paint.PaintBoard
import ir.amirroid.canvas.ui.components.paint.rememberPaintState
import ir.amirroid.canvas.ui.icons.LineIcon
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
        when (state) {
            is PaintScreen.State.Loading -> {
                PaintLoadingContent(modifier)
            }

            is PaintScreen.State.Success -> {
                PaintContent(state, modifier)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintContent(state: PaintScreen.State.Success, modifier: Modifier = Modifier) {
    val paintState = rememberPaintState()

    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(state.paintWithCanvasDocument.document.name)
            },
            navigationIcon = {
                IconButton(onClick = {
                    state.eventSink.invoke(PaintScreen.Event.Back)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )
        Card(modifier = Modifier.weight(1f).padding(12.dp)) {
            PaintBoard(
                state = paintState,
                modifier = Modifier.fillMaxSize()
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
                            paintState.currentCanvasType = type
                        },
                        selected = paintState.currentCanvasType == type
                    )
                }
                SectionIconButton(
                    icon = Icons.AutoMirrored.Rounded.Undo,
                    onClick = paintState::undo,
                    enabled = paintState.undoList.isNotEmpty()
                )
                SectionIconButton(
                    icon = Icons.AutoMirrored.Rounded.Redo,
                    onClick = paintState::redo,
                    enabled = paintState.redoList.isNotEmpty()
                )
                SectionIconButton(
                    icon = Icons.Rounded.Delete,
                    onClick = paintState::clearAll,
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
fun PaintLoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        LoadingIndicator(modifier = Modifier.size(88.dp))
    }
}