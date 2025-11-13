package ir.amirroid.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ir.amirroid.canvas.features.main.MainNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val appComponent = AndroidAppComponent::class.create(applicationContext)

        setContent {
            MainNavigation(circuit = appComponent.circuit)
        }
    }
}