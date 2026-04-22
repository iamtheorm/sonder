import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private data class DashboardFeature(
    val title: String,
    val icon: ImageVector,
    val isPremiumLocked: Boolean
)

@Composable
fun HomeScreen(viewModel: SonderViewModel) {
    val userType by viewModel.userType.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    var showChatbotSheet by remember { mutableStateOf(false) }

    val features = buildList {
        add(
            DashboardFeature(
                title = "Monthly Tracking",
                icon = Icons.Filled.Assessment,
                isPremiumLocked = false
            )
        )

        if (userType == UserRole.SCHOOL) {
            add(
                DashboardFeature(
                    title = "Career Guidance",
                    icon = Icons.Filled.Lightbulb,
                    isPremiumLocked = !isPremium
                )
            )
        }

        add(
            DashboardFeature(
                title = "Aptitude & Alignment",
                icon = Icons.Filled.Psychology,
                isPremiumLocked = !isPremium
            )
        )
        add(
            DashboardFeature(
                title = "Journaling",
                icon = Icons.Filled.MenuBook,
                isPremiumLocked = !isPremium
            )
        )
        add(
            DashboardFeature(
                title = "Community",
                icon = Icons.Filled.Groups,
                isPremiumLocked = !isPremium
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home Dashboard") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showChatbotSheet = true },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    imageVector = Icons.Filled.Chat,
                    contentDescription = "Open AI Chatbot",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(features) { feature ->
                FeatureCard(
                    title = feature.title,
                    icon = feature.icon,
                    isPremiumLocked = feature.isPremiumLocked
                )
            }
        }
    }

    if (showChatbotSheet) {
        ModalBottomSheet(
            onDismissRequest = { showChatbotSheet = false }
        ) {
            ChatbotScreen()
        }
    }
}

@Composable
fun FeatureCard(title: String, icon: ImageVector, isPremiumLocked: Boolean) {
    val cardColor = if (isPremiumLocked) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardColor)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (isPremiumLocked) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Premium Locked",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
