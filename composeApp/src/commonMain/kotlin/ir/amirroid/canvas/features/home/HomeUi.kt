package ir.amirroid.canvas.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import ir.amirroid.canvas.features.home.circuit.HomeScreen
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(HomeScreen::class, AppScope::class)
class HomeUi : Ui<HomeScreen.State> {
    @Composable
    override fun Content(
        state: HomeScreen.State,
        modifier: Modifier
    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(state.name)
        }
    }
}