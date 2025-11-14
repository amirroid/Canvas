package ir.amirroid.canvas.features.home

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import ir.amirroid.canvas.features.home.circuit.HomeScreen
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(HomeScreen::class, AppScope::class)
class HomeUi : Ui<HomeScreen.State> {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    override fun Content(
        state: HomeScreen.State,
        modifier: Modifier
    ) {
        LoadingIndicator()
    }
}