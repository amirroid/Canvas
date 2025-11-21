package ir.amirroid.canvas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.rememberDynamicMaterialThemeState

val SeedColor = Color(0xFFA1FF00)

//@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CanvasTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicThemeState = rememberDynamicMaterialThemeState(
        isDark = isDarkTheme,
        seedColor = SeedColor,
    )

    DynamicMaterialTheme(
        state = dynamicThemeState,
        animate = true,
        content = content,
//        motionScheme = MotionScheme.expressive()
    )
}