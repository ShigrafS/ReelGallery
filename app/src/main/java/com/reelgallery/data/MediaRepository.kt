package com.reelgallery.data

import com.reelgallery.domain.MediaFolder
import com.reelgallery.domain.MediaItem

interface MediaRepository {
    suspend fun getFolders(): List<MediaFolder>

    suspend fun getMediaByFolder(folderId: Long): List<MediaItem>
}

class MediaRepositoryImpl(
    private val dataSource: MediaStoreDataSource,
) : MediaRepository {
    // simple memory cache to avoid thrashing MediaStore
    private var cachedMedia: List<MediaItem>? = null

    private suspend fun getMediaCache(): List<MediaItem> {
        if (cachedMedia == null) {
            cachedMedia = dataSource.getAllMedia()
        }
        return cachedMedia!!
    }

    override suspend fun getFolders(): List<MediaFolder> {
        val allData = getMediaCache()

        // group by folder
        val grouped = allData.groupBy { it.folderId }

        val folderList =
            grouped.map { (fId, items) ->
                // use the first item as thumbnail and grab folder name
                val firstItem = items.first()
                MediaFolder(
                    id = fId,
                    name = firstItem.folderName,
                    thumbnailUri = firstItem.uri,
                    itemCount = items.size,
                )
            }
        // return sorted alphabetically by name
        return folderList.sortedBy { it.name.lowercase() }
    }

    override suspend fun getMediaByFolder(folderId: Long): List<MediaItem> {
        return getMediaCache().filter { it.folderId == folderId }
    }

    // helper to force refresh if we add upload/delete later
    fun clearCache() {
        cachedMedia = null
    }
}
