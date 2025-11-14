package ir.amirroid.canvas.features.add_paint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import ir.amirroid.canvas.domain.usecase.CreateNewPaintUseCase
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(AddNewPaintScreen::class, AppScope::class)
class AddNewPaintPresenter @Inject constructor(
    @Assisted private val navigator: Navigator,
    createNewPaintUseCase: Lazy<CreateNewPaintUseCase>
) : Presenter<AddNewPaintScreen.State> {
    private val createNewPaintUseCase by createNewPaintUseCase

    @Composable
    override fun present(): AddNewPaintScreen.State {
        var name by rememberSaveable { mutableStateOf("") }
        var selectedPath by rememberSaveable { mutableStateOf("") }
        var selectedFileName by rememberSaveable { mutableStateOf("") }
        val scope = rememberCoroutineScope()

        val filePicker = rememberFileSaverLauncher { file ->
            file?.let { file ->
                selectedPath = file.path
                selectedFileName = file.name
            }
        }

        return AddNewPaintScreen.State(
            name = name,
            selectedFileName = selectedFileName,
        ) { event ->
            when (event) {
                is AddNewPaintScreen.Event.ChangeName -> {
                    name = event.name
                }

                is AddNewPaintScreen.Event.OpenFilePicker -> {
                    filePicker.launch(
                        suggestedName = name.split(" ").joinToString(" "),
                        extension = "json"
                    )
                }

                is AddNewPaintScreen.Event.Back -> {
                    navigator.pop()
                }

                is AddNewPaintScreen.Event.Create -> {
                    scope.launch {
                        createNewPaintUseCase.invoke(name, selectedPath)
                        navigator.pop()
                    }
                }
            }
        }
    }
}