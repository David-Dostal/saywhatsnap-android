
# SayWhatSnap

SayWhatSnap (Czech: *TlumočMi*) is a Kotlin Android app designed to help travelers translate foreign text using their camera. With ML Kit and LibreTranslate, the app extracts text from images, translates it, and allows users to pin and manage translation locations on an interactive map.

## 📱 Features

- 📸 Capture or upload images to detect text (ML Kit)
- 🌐 Translate text using LibreTranslate API
- 📍 Pin translation locations on Google Maps
- 🕘 View and manage translation history
- 🗂 Edit pin details (name, category, visibility)
- 🗺️ Explore all translation locations visually on a map
- 💾 Save translations and app settings locally with Room and DataStore

## 🔧 App Structure

- **Intro screen**: Language selection
- **Camera screen**: Capture image for translation
- **Translation result screen**: View translation, retry, or save
- **History screen**: List of past translations
- **Map screen**: Interactive map with translation pins
- **Pin detail screen**: Edit metadata for saved pins
- **Settings**: App localization and preferences

## 🧰 Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM
- **Database**: Room (SQLite)
- **Text Recognition**: ML Kit
- **Translation**: LibreTranslate API
- **Location**: Google Maps SDK
- **Storage**: DataStore
- **DI**: Hilt
- **Build Tool**: Gradle (KTS)
- **Testing**: UI & Instrumentation Tests

## 🚀 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/saywhatsnap.git
   ```
2. Open in Android Studio
3. Run on an emulator or device with camera and Maps API enabled

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
