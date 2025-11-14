package ir.amirroid.canvas.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path

val LineIcon: ImageVector
    get() {
        if (_line != null) return _line!!

        _line = materialIcon(name = "Line") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(24f, 0f)
                lineTo(0f, 24f)
            }
        }

        return _line!!
    }

private var _line: ImageVector? = null