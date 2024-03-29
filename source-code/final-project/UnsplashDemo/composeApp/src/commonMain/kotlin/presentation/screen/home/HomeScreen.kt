package presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import presentation.component.ErrorScreen
import presentation.component.LoadingScreen
import presentation.component.PhotoCard
import presentation.component.ZoomableImage
import util.DisplayResult

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { HomeViewModel() }
        val data by viewModel.data
        var fullScreenImage: String? by remember { mutableStateOf(null) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (fullScreenImage != null) "Back"
                            else "Photos"
                        )
                    },
                    navigationIcon = {
                        if (fullScreenImage != null) {
                            IconButton(onClick = { fullScreenImage = null }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Back Arrow Icon"
                                )
                            }
                        }
                    }
                )
            }
        ) { padding ->
            data.DisplayResult(
                onLoading = { LoadingScreen() },
                onError = { ErrorScreen(message = it) },
                onSuccess = { photos ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = padding.calculateTopPadding())
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(40.dp)
                        ) {
                            items(
                                items = photos.items,
                                key = { it.id }
                            ) {
                                PhotoCard(
                                    photo = it,
                                    onClick = { image ->
                                        fullScreenImage = image
                                    }
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = fullScreenImage != null,
                            enter = fadeIn(tween(durationMillis = 300)),
                            exit = fadeOut(tween(durationMillis = 300))
                        ) {
                            ZoomableImage(link = fullScreenImage)
                        }
                    }
                }
            )
        }
    }
}