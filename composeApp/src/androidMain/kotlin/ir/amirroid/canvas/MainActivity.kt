package ir.amirroid.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import ir.amirroid.canvas.di.AndroidAppComponent
import ir.amirroid.canvas.di.create
import ir.amirroid.canvas.features.home.HomeScreen
import ir.amirroid.canvas.features.main.MainNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val appComponent = AndroidAppComponent::class.create(applicationContext)



        setContent {
            val backstack = rememberSaveableBackStack(HomeScreen)
            val navigator = rememberCircuitNavigator(backstack)

            MainNavigation(
                circuit = appComponent.circuit,
                backstack = backstack,
                navigator = navigator
            )

        }
    }
}