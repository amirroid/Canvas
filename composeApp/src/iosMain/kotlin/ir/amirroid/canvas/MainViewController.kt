package ir.amirroid.canvas

import androidx.compose.ui.window.ComposeUIViewController
import ir.amirroid.canvas.features.main.MainNavigation

fun MainViewController() = ComposeUIViewController { MainNavigation() }