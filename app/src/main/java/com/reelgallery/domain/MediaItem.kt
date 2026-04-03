package com.reelgallery.domain

import android.net.Uri

enum class MediaType {
    IMAGE,
    VIDEO,
}

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val type: MediaType,
    val duration: Long?, // Null for images
    val folderId: Long,
    val folderName: String,
    val dateAdded: Long,
)
