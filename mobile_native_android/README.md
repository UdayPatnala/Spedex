# SpeDex Native Android Application

## Overview
This directory contains the native Kotlin Android implementation of SpeDex, built using Jetpack Compose, Material 3, and modern Android architecture guidelines.

---

## 🛠️ Architecture & Tech Stack

- **UI Toolkit**: Jetpack Compose & Material 3
- **Language**: Kotlin 1.9+
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles
- **Persistence**: Room Database (`AppDatabase.kt`, `Daos.kt`, `Entities.kt`)
- **State Management**: Kotlin StateFlow & ViewModel (`SpedexViewModel.kt`)

---

## 📱 Features

- **Home Screen**: Daily spending summary, quick pay vendors, and active trip preview.
- **Trips Screen**: Active trip tracker, category distribution, and cash/card expense logger.
- **Budget Screen**: Weekly index, category budget bars, and savings insights.
- **Reminders Screen**: Bill and subscription reminders with one-tap status updates.
- **Scanner Screen**: Camera QR code scanner for instant UPI merchant payments.
- **Medieval Rank Helper**: Gamified financial achievement tier badges.

---

## 🚀 Building & Running

1. Open Android Studio and select `Open Project` -> choose `mobile_native_android`.
2. Ensure Android SDK 34 is installed.
3. Sync Gradle dependencies.
4. Run on an Android Emulator or connected physical device.
