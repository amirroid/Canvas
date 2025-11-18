package ir.amirroid.canvas.features.add_paint

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ir.amirroid.canvas.utils.annotations.CommonParcelize

@CommonParcelize
data object AddNewPaintScreen : Screen {
    @Immutable
    data class State(
        val name: String,
        val selectedFileName: String,
        val isAddNewButtonEnabled: Boolean,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data object OpenFilePicker : Event
        data class ChangeName(val name: String) : Event
        data object Create : Event
    }
}