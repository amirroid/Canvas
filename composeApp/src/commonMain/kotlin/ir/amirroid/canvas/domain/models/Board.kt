package ir.amirroid.canvas.domain.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Board(
    val width: Float,
    val height: Float,
) {
    val aspectRatio = width / height
}