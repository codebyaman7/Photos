# 📸 Photos

A premium, private, and ultra-smooth offline Android gallery app featuring a genuine Liquid Glass design system.

Developed by **Aman**.

---

## ✨ Highlights

* **Liquid Glass Design System:** High-end frosted glass aesthetic with subtle specular highlights, adaptive dynamic color theming, and real-time backdrop blur.
* **Ultra-Smooth 120Hz Performance:** Off-main-thread image decoding and zero-allocation thumbnail pipeline engineered for stutter-free scrolling across thousands of photos.
* **100% On-Device & Private:** Works completely offline. No tracking, no cloud telemetry, and zero data leaves your phone.
* **Smart Organization:** Timeline view categorized dynamically by Day, Month, and Year with custom pinch-to-zoom grid density.
* **Full-Screen Viewer:** Immersive photo viewer with smooth pinch-to-zoom, pan gestures, and detailed EXIF metadata inspector.
* **Smart Albums:** Automatic categorization for Camera, Screenshots, Downloads, Videos, and custom albums.
* **Safe Deletion & Favorites:** Integrated Trash with auto-recycle safeguards and one-tap Favorites.
* **Biometric Secure Folder:** Hardware-backed biometric lock for sensitive photos and media.

---

## 🔒 Privacy Guarantee

Photos is built with an absolute privacy-first mindset:
* All media reading, caching, and editing happen **100% locally** on your device.
* Zero cloud uploads, zero account logins, and zero advertising SDKs.
* Biometric credentials never leave Android's secure enclave (Keystore / Keymaster).

---

## 🛠 Tech Stack

* **Language & Architecture:** 100% Kotlin, Single-Activity MVVM Architecture
* **UI Toolkit:** Jetpack Compose + Material 3 + Custom Liquid Glass Shaders
* **Image & Video Pipeline:** Coil, Telephoto (deep pinch-to-zoom), Media3 / ExoPlayer
* **Database & Persistence:** Room Database, DataStore Preferences
* **Security:** AndroidX BiometricPrompt API

---

## 📱 Requirements

* **OS Version:** Android 12 (API level 31) or higher
* **Architecture:** Optimized for `arm64-v8a`

---

## 📥 Getting Started

1. Grab the latest APK release from the [Releases](releases) tab.
2. Install the APK on your Android device (ensure "Install from unknown sources" is allowed).
3. Open **Photos** and enjoy a smooth, private gallery experience.

---

### 👨‍💻 Developer
Developed with ❤️ by **Aman**
