package ir.amirroid.canvas.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ir.amirroid.canvas.domain.usecase.GetAllPaintWithCanvasesUseCase
import ir.amirroid.canvas.features.add_paint.AddNewPaintScreen
import me.tatarka.inject.annotations.Assisted
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(HomeScreen::class, AppScope::class)
class HomePresenter(
    private val getAllPaintWithCanvasesUseCase: GetAllPaintWithCanvasesUseCase,
    @Assisted private val navigator: Navigator
) : Presenter<HomeScreen.State> {
    @Composable
    override fun present(): HomeScreen.State {
        val paints by rememberRetained {
            getAllPaintWithCanvasesUseCase()
        }.collectAsRetainedState(emptyList())

        return HomeScreen.State(paints = paints) { event ->
            when (event) {
                HomeScreen.Event.NavigateToAddNewPaint -> {
                    navigator.goTo(AddNewPaintScreen)
                }
            }
        }
    }
}