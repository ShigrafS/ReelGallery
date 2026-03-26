# 🎞️ ReelGallery

<div align="center">

A premium, lightweight Android gallery built with **Jetpack Compose** for browsing folders, viewing media in grids, and watching videos in a modern, vertical reel-style viewer.

[![Android CI](https://github.com/ShigrafS/ReelGallery/actions/workflows/ci.yml/badge.svg)](https://github.com/ShigrafS/ReelGallery/actions/workflows/ci.yml)
![Platform](https://img.shields.io/badge/platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Media](https://img.shields.io/badge/player-Media3%20ExoPlayer-FF6F00?style=for-the-badge)

---

![ReelGallery Mockup](file:///C:/Users/user/.gemini/antigravity/brain/b1603f45-2527-4ff4-8a35-1713fd3c42d4/reelgallery_mockup_v1_1774542860580.png)

</div>

## ✨ Key Features

- 📁 **Folder-Centric Browsing**: Seamlessly navigate local media folders via `MediaStore`.
- 🧩 **Optimized Media Grids**: Fast, 3-column views with intelligent image caching.
- 📱 **Premium Reel Viewer**: Full-screen, vertical paging experience with glassmorphism overlays.
- ▶️ **Auto-Looping Video**: Smooth playback powered by **AndroidX Media3**.
- ⚡ **Performance Optimized**: Low-RAM footprint, targeting Android 13+ (API 33).
- 🔐 **Privacy First**: Fully offline, on-device media handling.

## 🧱 Modern Tech Stack

| Area | Technology |
|---|---|
| **Language** | Kotlin (Coroutines + Flow) |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Media Engine** | AndroidX Media3 (ExoPlayer) |
| **Image Loading** | Coil (Compose-optimized) |
| **Architecture** | MVVM-inspired layers (Data, Domain, UI) |
| **Build System** | Gradle (Kotlin DSL) with **Gradle Wrapper** |
| **Static Analysis** | Detekt + Ktlint + Android Lint |

## 🏗️ Project Structure

```text
app/src/main/java/com/reelgallery
├── data/          # Repository and MediaStore integration
├── domain/        # Business logic and entity models
├── player/        # Centralized Media3 player lifecycle control
├── ui/           
│   ├── navigation/ # State-based navigation handling
│   ├── screens/    # Jetpack Compose UI screens (Folder, Grid, Reel)
│   └── theme/      # Material 3 Design System
├── viewmodel/     # State holders and logic separation
└── MainActivity.kt
```

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Hedgehog or higher)
- **JDK 17+**
- Android SDK (Target: API 34, Min: API 26)

### Build Locally
The repository includes the **Gradle Wrapper**, so no local installation of Gradle is required:

```bash
# Clone the repository
git clone https://github.com/ShigrafS/ReelGallery.git

# Navigate to the project directory
cd ReelGallery

# Build the Debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

## ⚙️ CI/CD Pipeline
The project uses **GitHub Actions** for robust multi-platform quality assurance. Every commit and pull request triggers a build matrix across:
- 🐧 **Ubuntu**
- 🍎 **macOS**
- 🪟 **Windows**

### Automated Quality Checks
- ✅ **Build Verification**: Ensures the app compiles on all platforms.
- 🛠️ **Artifact Upload**: Automatically uploads the `app-debug.apk` for testing.
- 🧪 **Unit Testing**: Runs the complete JUnit test suite.
- 🔍 **Static Analysis**: Enforces standards via **Detekt**, **Ktlint**, and **Android Lint**.

## 🤝 Contribution Guidelines

This project maintains high standards for code quality and documentation:
1. **DCO Sign-off**: All commits must be signed off (`git commit -s`).
2. **Conventional Commits**: Use the Angular/Conventional Commit style (`feat:`, `fix:`, `refactor:`, etc.).
3. **Pre-commit Hooks**: Ensure local checks pass before pushing (`pre-commit install`).

---

## 📄 License
This project is currently distributed without a formal license. See `LICENSE` for future updates.
