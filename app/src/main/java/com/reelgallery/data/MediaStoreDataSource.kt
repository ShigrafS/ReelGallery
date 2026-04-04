package com.reelgallery.data

import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.reelgallery.domain.MediaItem
import com.reelgallery.domain.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaStoreDataSource(private val context: Context) {
    // TODO: support pagination for huge folders later? MVP just loads all.
    suspend fun getAllMedia(): List<MediaItem> =
        withContext(Dispatchers.IO) {
            val mediaList = mutableListOf<MediaItem>()

            // hacky fix: some weird devices return null for BUCKET_DISPLAY_NAME so we fallback to "Unknown"
            val projection =
                arrayOf(
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.DURATION,
                    MediaStore.Files.FileColumns.BUCKET_ID,
                    MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Files.FileColumns.DATE_ADDED,
                )

            val selection = "\${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR \${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
            val selectionArgs =
                arrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
                )

            val queryUri = MediaStore.Files.getContentUri("external")

            val queryArgs = Bundle().apply {
                putString(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                putStringArray(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
                putStringArray(
                    android.content.ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Files.FileColumns.DATE_ADDED)
                )
                putInt(
                    android.content.ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    android.content.ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
            }

            context.contentResolver.query(
                queryUri,
                projection,
                queryArgs,
                null,
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val typeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val durCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DURATION)
                val bucketIdCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID)
                val bucketNameCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val typeInt = cursor.getInt(typeCol)
                    val type = if (typeInt == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) MediaType.VIDEO else MediaType.IMAGE
                    val dur = if (type == MediaType.VIDEO) cursor.getLong(durCol) else null
                    val folderId = cursor.getLong(bucketIdCol)
                    val folderName = cursor.getString(bucketNameCol) ?: "Unknown" // fallback
                    val dateAdded = cursor.getLong(dateCol)

                    // the content uri depends on type
                    val baseUri =
                        if (type == MediaType.VIDEO) {
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        } else {
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                    val contentUri = ContentUris.withAppendedId(baseUri, id)

                    mediaList.add(
                        MediaItem(
                            id = id,
                            uri = contentUri,
                            type = type,
                            duration = dur,
                            folderId = folderId,
                            folderName = folderName,
                            dateAdded = dateAdded,
                        ),
                    )
                }
            }

            return@withContext mediaList
        }
}
