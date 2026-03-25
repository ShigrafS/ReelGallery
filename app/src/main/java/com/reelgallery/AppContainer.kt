package com.reelgallery

import android.content.Context
import com.reelgallery.data.MediaRepository
import com.reelgallery.data.MediaRepositoryImpl
import com.reelgallery.data.MediaStoreDataSource
import com.reelgallery.player.PlayerManager

/**
 * Super simple manual DI to avoid bloating MVP with Hilt/Dagger.
 */
class AppContainer(private val context: Context) {
    val mediaRepository: MediaRepository by lazy {
        MediaRepositoryImpl(MediaStoreDataSource(context.applicationContext))
    }
    
    val playerManager: PlayerManager by lazy {
        PlayerManager() // initialized when needed in Compose explicitly
    }
}
