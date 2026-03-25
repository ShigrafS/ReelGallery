package com.reelgallery

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reelgallery.ui.navigation.Screen
import com.reelgallery.ui.screens.FolderScreen
import com.reelgallery.ui.screens.GridScreen
import com.reelgallery.ui.screens.ReelViewerScreen
import com.reelgallery.ui.theme.ReelGalleryTheme
import com.reelgallery.viewmodel.FolderViewModel
import com.reelgallery.viewmodel.GridViewModel
import com.reelgallery.viewmodel.ReelViewModel
// simplistic navigation state for MVP
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer
    
    // simplistic nav state
    private var currentScreen by mutableStateOf<Screen>(Screen.Folders)

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            currentScreen = Screen.Folders
        } else {
            Toast.makeText(this, "Permissions required to view gallery", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainer(this)
        
        requestPermissions()

        setContent {
            ReelGalleryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        appContainer.playerManager.release()
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            )
        } else {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        }
    }

    @Composable
    private fun AppNavigation() {
        when (val screen = currentScreen) {
            is Screen.Folders -> {
                val factory = remember { FolderViewModel.Factory(appContainer.mediaRepository) }
                val viewModel: FolderViewModel = viewModel(factory = factory)
                FolderScreen(
                    viewModel = viewModel,
                    onFolderClick = { folderId -> currentScreen = Screen.Grid(folderId) }
                )
            }
            is Screen.Grid -> {
                val factory = remember(screen.folderId) { GridViewModel.Factory(appContainer.mediaRepository, screen.folderId) }
                val viewModel: GridViewModel = viewModel(factory = factory)
                GridScreen(
                    viewModel = viewModel,
                    onNavigateUp = { currentScreen = Screen.Folders },
                    onMediaClick = { index -> 
                        currentScreen = Screen.Reels(screen.folderId, index) 
                    }
                )
            }
            is Screen.Reels -> {
                val factory = remember(screen.folderId, screen.startIndex) {
                    ReelViewModel.Factory(appContainer.mediaRepository, screen.folderId, screen.startIndex)
                }
                val viewModel: ReelViewModel = viewModel(factory = factory)
                ReelViewerScreen(
                    viewModel = viewModel,
                    playerManager = appContainer.playerManager,
                    onNavigateUp = { currentScreen = Screen.Grid(screen.folderId) }
                )
            }
        }
    }
    
    // Handle back button for our simple manual navigation
    override fun onBackPressed() {
        when (val screen = currentScreen) {
            is Screen.Folders -> super.onBackPressed()
            is Screen.Grid -> currentScreen = Screen.Folders
            is Screen.Reels -> currentScreen = Screen.Grid(screen.folderId)
        }
    }
}
