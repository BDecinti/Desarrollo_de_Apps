package cl.duoc.disenamadera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.disenamadera.ui.navigation.AppRoot
import cl.duoc.disenamadera.ui.theme.DisenaMaderaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as DisenaMaderaApp
        setContent {
            DisenaMaderaTheme {
                AppRoot(app)
            }
        }
    }
}
