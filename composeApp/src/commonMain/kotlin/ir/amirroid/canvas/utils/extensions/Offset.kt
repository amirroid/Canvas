package ir.amirroid.canvas.utils.extensions

import androidx.compose.ui.geometry.Offset

fun List<Offset>.sortedByPosition(): List<Offset> {
    return this.sortedWith(
        compareBy<Offset> { it.x }
            .thenBy { it.y }
    )
}