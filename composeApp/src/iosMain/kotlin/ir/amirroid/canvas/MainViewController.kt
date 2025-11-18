package ir.amirroid.canvas

import androidx.compose.ui.window.ComposeUIViewController
import ir.amirroid.canvas.di.AppComponent
import ir.amirroid.canvas.di.IosAppComponent
import ir.amirroid.canvas.features.main.MainNavigation
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
//    val component = IosAppComponent::class.create()
    val controller = ComposeUIViewController {}
    return controller
}