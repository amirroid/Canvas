package ir.amirroid.canvas.features.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.BackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.runtime.Navigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
//import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import ir.amirroid.canvas.ui.theme.CanvasTheme


@Composable
fun <R : BackStack.Record> MainNavigation(
    circuit: Circuit,
    backstack: BackStack<R>,
    navigator: Navigator
) {
    CanvasTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            CircuitCompositionLocals(
                circuit = circuit
            ) {
                NavigableCircuitContent(
                    navigator = navigator,
                    backStack = backstack,
                    modifier = Modifier.fillMaxSize(),
                    decoratorFactory = remember(navigator) {
                        GestureNavigationDecorationFactory(
                            onBackInvoked = navigator::pop
                        )
                    }
                )
            }
        }
    }
}