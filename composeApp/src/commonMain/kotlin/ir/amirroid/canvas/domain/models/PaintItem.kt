package ir.amirroid.canvas.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class PaintItem(
    val id: Long = 0,
    val fileUri: String
)