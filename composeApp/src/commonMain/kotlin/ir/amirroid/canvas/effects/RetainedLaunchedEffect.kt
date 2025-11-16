package ir.amirroid.canvas.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import kotlinx.coroutines.CoroutineScope

@Composable
fun RetainedLaunchedEffect(vararg keys: Any?, block: suspend CoroutineScope.() -> Unit) {
    var isConsumed by rememberRetained(*keys) {
        mutableStateOf(false)
    }

    LaunchedEffect(*keys) {
        if (isConsumed.not()) {
            isConsumed = true
            block.invoke(this)
        }
    }
}