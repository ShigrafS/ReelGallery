package com.reelgallery.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

@OptIn(UnstableApi::class)
class PlayerManager {
    var exoPlayer: ExoPlayer? = null
        private set

    private var currentUri: Uri? = null

    fun initialize(context: Context) {
        if (exoPlayer == null) {
            exoPlayer =
                ExoPlayer.Builder(context).build().apply {
                    repeatMode = Player.REPEAT_MODE_ONE
                    playWhenReady = true // auto-play
                }
        }
    }

    fun play(uri: Uri) {
        if (currentUri == uri) {
            // already playing this or paused, just resume
            exoPlayer?.play()
            return
        }

        currentUri = uri
        exoPlayer?.apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            play()
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
        currentUri = null
    }
}
