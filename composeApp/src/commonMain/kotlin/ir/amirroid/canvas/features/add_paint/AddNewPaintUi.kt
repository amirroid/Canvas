package ir.amirroid.canvas.features.add_paint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import canvas.composeapp.generated.resources.Res
import canvas.composeapp.generated.resources.add_new_paint
import canvas.composeapp.generated.resources.create
import canvas.composeapp.generated.resources.file
import canvas.composeapp.generated.resources.name
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(AddNewPaintScreen::class, AppScope::class)
class AddNewPaintUi : Ui<AddNewPaintScreen.State> {
    @Composable
    override fun Content(
        state: AddNewPaintScreen.State,
        modifier: Modifier
    ) {
        AddNewPaintContent(state, modifier)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPaintContent(state: AddNewPaintScreen.State, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(stringResource(Res.string.add_new_paint))
            },
            navigationIcon = {
                IconButton(onClick = {
                    state.eventSink.invoke(AddNewPaintScreen.Event.Back)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )
        OutlinedTextField(
            value = state.name,
            onValueChange = {
                state.eventSink.invoke(AddNewPaintScreen.Event.ChangeName(it))
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            label = {
                Text(stringResource(Res.string.name))
            }
        )
        OutlinedTextField(
            value = state.selectedFileName,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            label = {
                Text(stringResource(Res.string.file))
            },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    state.eventSink.invoke(AddNewPaintScreen.Event.OpenFilePicker)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Folder,
                        contentDescription = null
                    )
                }
            }
        )
        Button(
            onClick = {
                state.eventSink.invoke(AddNewPaintScreen.Event.Create)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            enabled = state.isAddNewButtonEnabled
        ) {
            Text(stringResource(Res.string.create))
        }
    }
}