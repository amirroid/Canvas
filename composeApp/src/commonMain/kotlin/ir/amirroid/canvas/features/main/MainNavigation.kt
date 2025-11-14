package ir.amirroid.canvas.features.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import ir.amirroid.canvas.features.home.circuit.HomeScreen


@Composable
fun MainNavigation(circuit: Circuit) {
    val backstack = rememberSaveableBackStack(HomeScreen)
    val navigator = rememberCircuitNavigator(backstack) {}

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