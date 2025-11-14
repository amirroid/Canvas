package ir.amirroid.canvas.features.home.circuit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import ir.amirroid.canvas.domain.usecase.GetAllPaintWithCanvasesUseCase
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(HomeScreen::class, AppScope::class)
@Inject
class HomePresenter(
    private val getAllPaintWithCanvasesUseCase: GetAllPaintWithCanvasesUseCase
) : Presenter<HomeScreen.State> {
    @Composable
    override fun present(): HomeScreen.State {
        val paints by rememberRetained {
            getAllPaintWithCanvasesUseCase()
        }.collectAsRetainedState(emptyList())

        return HomeScreen.State(paints = paints)
    }
}