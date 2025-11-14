package ir.amirroid.canvas.features.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import ir.amirroid.canvas.features.home.circuit.HomeScreen
import ir.amirroid.canvas.ui.theme.CanvasTheme


@Composable
fun MainNavigation(circuit: Circuit) {
    val backstack = rememberSaveableBackStack(HomeScreen)
    val navigator = rememberCircuitNavigator(backstack) {}

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
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}