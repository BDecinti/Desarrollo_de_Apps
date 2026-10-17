package cl.duoc.disenamadera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cl.duoc.disenamadera.ui.theme.DisenaMaderaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DisenaMaderaTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Placeholder de la etapa 1; la navegación real llega en la etapa 3
                    Box(contentAlignment = Alignment.Center) {
                        Text("Diseña Madera Móvil", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}
