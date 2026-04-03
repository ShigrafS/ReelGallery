package com.reelgallery.ui.navigation

sealed class Screen(val route: String) {
    object Folders : Screen("folders")

    // Use arguments in route like "grid/{folderId}" for simplicity in Compose Nav later,
    // but building our own simplified sealed class since PRD said no bloated extra libs if possible.
    // Compose Navigation is standard though, but standard simple state is faster for MVP.
    data class Grid(val folderId: Long) : Screen("grid_\$folderId")

    data class Reels(val folderId: Long, val startIndex: Int) : Screen("reels_\$folderId_\$startIndex")
}
