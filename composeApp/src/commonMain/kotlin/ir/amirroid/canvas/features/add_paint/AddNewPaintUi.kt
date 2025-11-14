package ir.amirroid.canvas.features.add_paint

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(AddNewPaintScreen::class, AppScope::class)
class AddNewPaintUi : Ui<AddNewPaintScreen.State> {
    @Composable
    override fun Content(
        state: AddNewPaintScreen.State,
        modifier: Modifier
    ) {

    }
}