package ir.amirroid.canvas.domain.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class CanvasType {
    LINE, PATH, RECT, OVAL, CLEAR
}

@Immutable
@Serializable
data class CanvasDocument(
    val name: String,
    val elements: List<CanvasElement>
) {
    @Immutable
    @Serializable
    data class CanvasElement(
        val type: CanvasType,
        val data: List<Double>,
        @SerialName("stroke_width")
        val strokeWidth: Float
    )
}