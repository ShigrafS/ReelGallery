# 🎞️ ReelGallery

<div align="center">

A lightweight, modern Android gallery app built with **Jetpack Compose** for browsing folders, viewing media in grids, and watching videos in a reel-style full-screen viewer.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?logo=kotlin&logoColor=white)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![Media](https://img.shields.io/badge/player-Media3%20ExoPlayer-FF6F00)

</div>

---

## ✨ Features

- 📁 **Folder-first browsing** of local media from `MediaStore`
- 🧩 **3-column media grid** per folder
- 📱 **Full-screen reel viewer** with vertical paging
- ▶️ **Video autoplay + loop** via Media3/ExoPlayer
- 🖼️ **Fast image loading** with Coil
- ⚡ **Lightweight MVP architecture** with manual DI and simple navigation state

---

## 🧱 Tech Stack

| Area | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM-inspired layers (data/domain/ui/viewmodel) |
| Media playback | AndroidX Media3 ExoPlayer |
| Image loading | Coil (Compose) |
| Build system | Gradle (Kotlin DSL) |
| Static checks | ktlint + detekt (via pre-commit hooks) |

---

## 🏗️ Project Structure

```text
app/src/main/java/com/reelgallery
├── data
│   ├── MediaRepository.kt
│   └── MediaStoreDataSource.kt
├── domain
│   ├── MediaFolder.kt
│   └── MediaItem.kt
├── player
│   └── PlayerManager.kt
├── ui
│   ├── navigation/AppNavigation.kt
│   ├── screens
│   │   ├── FolderScreen.kt
│   │   ├── GridScreen.kt
│   │   └── ReelViewerScreen.kt
│   └── theme/Theme.kt
├── viewmodel
│   ├── FolderViewModel.kt
│   ├── GridViewModel.kt
│   └── ReelViewModel.kt
├── AppContainer.kt
├── MainActivity.kt
└── ReelGalleryApp.kt
```

---

## 🧭 App Flow

1. **Folders screen** → lists media folders
2. **Grid screen** → shows folder contents in a 3-column layout
3. **Reels screen** → opens selected item in a vertical full-screen viewer

Back navigation is handled with a simple sealed-screen state in `MainActivity`.

---

## 🔐 Permissions

The app reads local media files and requests runtime permissions:

- Android 13+ (`API 33+`):
  - `READ_MEDIA_IMAGES`
  - `READ_MEDIA_VIDEO`
- Android 12 and below:
  - `READ_EXTERNAL_STORAGE`

---

## 📋 Requirements

- **Android Studio** (latest stable recommended)
- **JDK 17**
- Android SDK configured with:
  - `compileSdk = 34`
  - `minSdk = 26`
  - `targetSdk = 34`

> Note: This repository currently does **not** include a `gradlew` wrapper script.
> Use a local Gradle installation (**Gradle 8.2 or higher** recommended for this project setup) or run from Android Studio.

---

## 🚀 Build & Run

From the repository root:

```bash
gradle assembleDebug
```

or open the project in Android Studio and run the `app` configuration on an emulator/device.

### Common Commands

```bash
gradle assemble          # Assemble variants
gradle build             # Full project build
gradle test              # Unit tests (if present)
gradle connectedAndroidTest  # Instrumented tests (requires device/emulator)
```

---

## ✅ Pre-commit & Quality Checks

Configured in `.pre-commit-config.yaml`:

- `ktlint` formatting/check
- `detekt` static analysis
- trailing whitespace / EOF fixers
- YAML checks
- local hooks:
  - block `.log` / `LOG.md` file commits
  - DCO sign-off reminder

Install hooks locally:

```bash
pre-commit install
```

Run all hooks manually:

```bash
pre-commit run --all-files
```

---

## 🎯 Design Notes

- **Performance-minded image loading**: custom Coil memory/disk cache tuning in `ReelGalleryApp`
- **Simple in-memory media cache** in `data/MediaRepository.kt` to reduce repeated `MediaStore` queries
- **Player lifecycle control** centralized in `PlayerManager`

---

## 📄 License

No license file is currently included in this repository.
