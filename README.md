# Morse Translator Pro

A modern, fully offline Morse code toolkit for Android. Encode text to Morse,
transmit it through the flashlight, speaker or vibration motor, and decode
flashing lights or beeps back into text in real time.

Built with **Kotlin**, **Jetpack Compose**, **Material 3** and an **MVVM**
architecture. Works with no internet connection and no account.

![CI](https://github.com/OWNER/morse-translator-pro/actions/workflows/android.yml/badge.svg)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

> Replace `OWNER` in the badge URLs with your GitHub username/org after pushing.

---

## Features

- **Text → Morse** – convert any text into valid International (ITU) Morse code.
- **Morse → Text** – decode `.` / `-` / `/` notation back into readable text.
- **Transmit**
  - **Flashlight** – blink the camera LED torch with precise ITU timing.
  - **Audio** – generate clean sine-wave beeps (400–1200 Hz, adjustable volume).
  - **Vibration** – play Morse as a haptic waveform.
  - Play / Pause / Stop / Repeat, with Slow / Medium / Fast presets and a
    1–60 WPM custom slider (PARIS standard).
- **Decode**
  - **Camera** – detect flashing lights via CameraX luminance analysis.
  - **Microphone** – detect beeps with a Goertzel tone-energy filter and
    adaptive noise thresholding.
  - Live decoded text + Morse + a confidence score.
- **History** – Room-backed storage with search, favorites and delete.
- **Sharing** – share via SMS, WhatsApp, Telegram, Email or copy to clipboard.
- **Accessibility** – dark mode, dynamic Material You color, large-text option,
  haptics and screen-reader content descriptions.
- **Free + Pro** – core features are free; Pro (one-time purchase via Google
  Play Billing) unlocks advanced extras.

## Screenshots

See [`marketing/screenshots/`](marketing/screenshots/) for store-ready captures
(add device captures here before publishing).

## Architecture

```
UI (Jetpack Compose, Material 3)
   │  collectAsStateWithLifecycle
ViewModels (MVVM, StateFlow)
   │
Domain / core  ──  Data layer
  MorseCode       Room (history)
  MorseTiming     DataStore (settings)
  SignalDecoder   Billing
  Goertzel/DSP
   │
Platform: CameraX, AudioTrack/AudioRecord, Camera2 torch, Vibrator
```

The `core/` and `decoder/` packages contain **pure, Android-free logic** so the
encoding, timing and DSP are fully unit-testable on the JVM. See
[`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for details and diagrams.

## Tech stack

| Concern        | Choice                                   |
|----------------|------------------------------------------|
| Language       | Kotlin 2.0                               |
| UI             | Jetpack Compose + Material 3             |
| Architecture   | MVVM, unidirectional state (StateFlow)   |
| Persistence    | Room (history), DataStore (settings)     |
| Camera         | CameraX                                  |
| Audio          | AudioTrack (tone), AudioRecord + Goertzel|
| Billing        | Google Play Billing 7                    |
| Min / Target   | Android 12 (API 31) / API 35             |

## Building

Prerequisites: JDK 17+ and the Android SDK (API 35, build-tools 35).

```bash
# Debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest

# Run instrumented tests (needs a connected device / emulator)
./gradlew connectedDebugAndroidTest

# Lint
./gradlew lintDebug

# Release bundle (requires signing config, see docs/RELEASE.md)
./gradlew bundleRelease
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## Testing

- **Unit tests** (`app/src/test`): Morse encode/decode round-trips, ITU timing,
  signal decoding with jitter, Goertzel frequency selectivity, pulse collection.
- **Instrumented tests** (`app/src/androidTest`): Room DAO behavior and a
  Compose UI test for the encode screen.

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [User guide](docs/USER_GUIDE.md)
- [Release process](docs/RELEASE.md)
- [Privacy policy](docs/PRIVACY_POLICY.md)
- [Project report](docs/PROJECT_REPORT.md)

## Contributing

Issues and pull requests are welcome. Please run `./gradlew lintDebug testDebugUnitTest`
before submitting.

## License

Released under the [MIT License](LICENSE).
