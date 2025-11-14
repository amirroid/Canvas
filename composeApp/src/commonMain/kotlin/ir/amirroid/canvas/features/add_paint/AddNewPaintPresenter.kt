package ir.amirroid.canvas.features.add_paint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(AddNewPaintScreen::class, AppScope::class)
class AddNewPaintPresenter : Presenter<AddNewPaintScreen.State> {
    @Composable
    override fun present(): AddNewPaintScreen.State {
        var name by rememberSaveable { mutableStateOf("") }

        return AddNewPaintScreen.State(
            name = name
        )
    }
}