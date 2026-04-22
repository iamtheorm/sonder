import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

private val SonderBlue = Color(0xFF2F2FE4)
private val SonderBlueDeep = Color(0xFF162E93)
private val SonderIndigo = Color(0xFF1A1953)
private val SonderNight = Color(0xFF080616)

private val SonderLightColors = lightColorScheme(
    primary = SonderBlue,
    onPrimary = Color.White,
    primaryContainer = SonderBlueDeep,
    onPrimaryContainer = Color.White,
    secondary = SonderBlueDeep,
    onSecondary = Color.White,
    secondaryContainer = SonderIndigo.copy(alpha = 0.16f),
    onSecondaryContainer = SonderIndigo,
    tertiary = SonderIndigo,
    onTertiary = Color.White,
    background = Color(0xFFF6F7FF),
    onBackground = SonderNight,
    surface = Color.White,
    onSurface = SonderNight,
    surfaceVariant = Color(0xFFE9ECFF),
    onSurfaceVariant = SonderIndigo,
    outline = SonderBlueDeep.copy(alpha = 0.35f)
)

private val SonderDarkColors = darkColorScheme(
    primary = SonderBlue,
    onPrimary = Color.White,
    primaryContainer = SonderBlueDeep,
    onPrimaryContainer = Color(0xFFE4E8FF),
    secondary = Color(0xFF7B8DFF),
    onSecondary = SonderNight,
    secondaryContainer = SonderIndigo,
    onSecondaryContainer = Color(0xFFE2E4FF),
    tertiary = Color(0xFFA8B2FF),
    onTertiary = SonderNight,
    background = SonderNight,
    onBackground = Color(0xFFEAEAFF),
    surface = Color(0xFF0E0D24),
    onSurface = Color(0xFFEAEAFF),
    surfaceVariant = Color(0xFF171740),
    onSurfaceVariant = Color(0xFFC9CCFF),
    outline = Color(0xFF6368B3)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SonderTheme {
                SonderApp()
            }
        }
    }
}

@Composable
fun SonderApp() {
    val navController = rememberNavController()
    val sonderViewModel: SonderViewModel = viewModel()

    SonderNavigation(
        navController = navController,
        viewModel = sonderViewModel
    )
}

@Composable
fun SonderTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) SonderDarkColors else SonderLightColors
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
