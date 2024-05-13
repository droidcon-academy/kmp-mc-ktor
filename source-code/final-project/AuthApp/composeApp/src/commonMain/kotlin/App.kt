import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.Settings
import data.api.ApiService
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val settings = remember { Settings() }
        val apiService = remember { ApiService(settings = settings) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { scope.launch { apiService.signIn() } }) {
                Text(text = "Sign in")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { scope.launch { apiService.getCurrentUser() } }) {
                Text(text = "Get user")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { scope.launch { apiService.refreshToken() } }) {
                Text(text = "Refresh Token")
            }
        }
    }
}