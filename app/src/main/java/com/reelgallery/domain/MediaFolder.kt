package com.reelgallery.domain

import android.net.Uri

data class MediaFolder(
    val id: Long,
    val name: String,
    val thumbnailUri: Uri,
    val itemCount: Int
)
