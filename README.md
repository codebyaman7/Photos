# 📸 Photos - Liquid Glass Gallery

A premium, private, and ultra-smooth offline Android gallery app featuring a genuine Liquid Glass design system.

Developed with ❤️ by **Aman** ([@codebyaman7](https://github.com/codebyaman7))

---

## ✨ Features & Highlights

* 🫧 **Genuine Liquid Glass UI:** Frosted glass aesthetic with real-time backdrop blur, specular rim highlights, and dynamic adaptive theming.
* ⚡ **Ultra-Smooth 120Hz Performance:** Off-main-thread image decoding and zero-allocation thumbnail caching pipeline engineered for buttery smooth scrolling across thousands of photos without frame drops.
* 🔒 **100% On-Device Privacy:** Fully offline. Zero trackers, zero cloud telemetry, and zero analytics. Your media never leaves your device.
* 📅 **Smart Organization:** Timeline categorized seamlessly by Day, Month, and Year with interactive pinch-to-zoom grid scaling.
* 🔍 **Immersive Full-Screen Viewer:** Deep pinch-to-zoom, fluid swipe-to-dismiss gesture, video playback, and rich EXIF metadata viewer.
* 📁 **Smart Albums:** Automatic auto-detection for Camera, Screenshots, Downloads, Videos, and custom folders.
* 🗑️ **Safe Deletion & Favorites:** Integrated Trash with 30-day auto-empty alongside a Favorites feature.
* 🔐 **Biometric Secure Folder:** Hardware-backed biometric lock (fingerprint / face / device credential) to secure private photos locally.

---

## 🔒 Privacy Guarantee

Photos is built with an absolute privacy-first mindset:
* All media scanning, thumbnail generation, and photo operations run **100% locally** on your device.
* No account login, no ads, and no external cloud servers.
* Biometrics are processed strictly via Android's secure hardware enclave (`BiometricPrompt`).

---

## 🛠 Tech Stack

* **Language:** 100% Kotlin
* **UI Toolkit:** Jetpack Compose + Material 3 + Custom Liquid Glass Shaders
* **Image Pipeline:** Coil, Telephoto (deep zoomable image engine)
* **Media Playback:** Media3 / ExoPlayer
* **Database & Cache:** Room Database, DataStore Preferences, Native NIO Mapped Memory Cache
* **Security:** AndroidX Biometric API

---

## 📱 Requirements

* **Android Version:** Android 12 (API 31) or higher
* **Architecture:** Optimized for `arm64-v8a`

---

## 📥 Download Release APK

Download the latest optimized release APK directly from the **[Releases](https://github.com/codebyaman7/Photos/releases)** tab.

1. Download `Photos-arm64-v8a.apk`.
2. Enable "Install from unknown sources" if prompted.
3. Open **Photos** and enjoy a private, butter-smooth gallery!

---

### 👨‍💻 Author
**Aman**  
GitHub: [@codebyaman7](https://github.com/codebyaman7)
