package com.reelgallery.ui.screens

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.reelgallery.domain.MediaType
import com.reelgallery.player.PlayerManager
import com.reelgallery.viewmodel.ReelViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReelViewerScreen(
    viewModel: ReelViewModel,
    playerManager: PlayerManager,
    onNavigateUp: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Snapping list state
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = state.currentIndex)
    
    // observe exactly one visible item
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { index ->
                viewModel.onVisibleItemChanged(index)
            }
    }
    
    // handle exoPlayer playback bound to current visible item
    LaunchedEffect(state.currentIndex, state.media) {
        if (state.media.isNotEmpty() && state.currentIndex in state.media.indices) {
            val item = state.media[state.currentIndex]
            if (item.type == MediaType.VIDEO) {
                playerManager.initialize(context)
                playerManager.play(item.uri)
            } else {
                playerManager.pause()
            }
        }
    }
    
    // Cleanup player when we leave screen
    DisposableEffect(Unit) {
        onDispose {
            playerManager.pause()
            // Optionally could release() if we never reuse, but pause is safer for fast navigation
        }
    }

    if (state.media.isEmpty()) {
       // fallback
       Text("No Media")
    } else {
        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(state.media, key = { _, item -> item.id }) { index, item ->
                Box(
                    modifier = Modifier
                        .fillParentMaxSize() // Force 100% height and width
                ) {
                    if (item.type == MediaType.VIDEO) {
                        // Only attach player to the active view loosely
                        if (index == state.currentIndex) {
                            AndroidView(
                                factory = { ctx ->
                                    PlayerView(ctx).apply {
                                        player = playerManager.exoPlayer
                                        useController = false
                                        // fit mode
                                    }
                                },
                                update = { view ->
                                    // if reconstructed, ensure player attached
                                    if (view.player != playerManager.exoPlayer) {
                                        view.player = playerManager.exoPlayer
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Video placeholder/thumbnail when scrolled away
                            AsyncImage(
                                model = item.uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        // Image zoom logic (simplified to AsyncImage for MVP, pointerInput later if needed)
                        AsyncImage(
                            model = item.uri,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
