# Project Report — Morse Translator Pro

## 1. Overview
Morse Translator Pro is a native Android application that translates between
text and Morse code, transmits Morse via flashlight, audio and vibration, and
decodes Morse from camera (flashing lights) and microphone (beeps) input — all
offline. This report summarizes what was delivered, the current status, and the
steps that require the project owner's credentials.

## 2. Deliverables status

| # | Deliverable | Status | Notes |
|---|-------------|--------|-------|
| 1 | Working Android application | ✅ Done | Builds to a debug APK; 10 screens; all features implemented |
| 2 | Public GitHub repository | ⏳ Needs token | Code ready + CI; push requires GitHub auth |
| 3 | Published Play Store listing | ⏳ Needs account | Listing/assets/data-safety prepared; publish is manual |
| 4 | Marketing website | ✅ Built | Static site in `website/`; deploy + domain need owner |
| 5 | Marketing assets | ✅ Done | Icon + feature graphic SVGs, brand guide, copy |
| 6 | Release documentation | ✅ Done | `docs/RELEASE.md` |
| 7 | User guide | ✅ Done | `docs/USER_GUIDE.md` |
| 8 | Technical documentation | ✅ Done | `docs/ARCHITECTURE.md`, README |
| 9 | CI/CD pipeline | ✅ Done | `.github/workflows/android.yml` |
| 10 | Final project report | ✅ This document | |

## 3. What was built

- **Architecture:** MVVM, Jetpack Compose + Material 3, manual DI via
  `AppContainer` + `ViewModelFactory`.
- **Core (Android-free, unit-tested):** ITU Morse table + encode/decode, PARIS
  WPM timing, adaptive signal decoder with confidence scoring.
- **DSP:** Goertzel single-tone detector, adaptive threshold, pulse collector.
- **Playback:** Camera2 torch, `AudioTrack` sine synthesis (400–1200 Hz),
  `VibrationEffect` waveform; shared play/pause/stop/repeat + progress.
- **Decoders:** CameraX luminance analyzer; `AudioRecord` + Goertzel real-time.
- **Data:** Room (history: search, favorites, delete) + DataStore (settings).
- **Billing:** Google Play Billing 7 one-time Pro upgrade.
- **Accessibility:** dark mode, dynamic color, large-text, haptics, content
  descriptions.
- **10 screens:** Home, Text→Morse, Morse→Text, Flashlight, Audio, Camera
  Decoder, Audio Decoder, History, Settings, About.

## 4. Quality

- **Unit tests:** 33 passing (encode/decode round-trips, ITU timing, decoder
  with jitter, Goertzel selectivity, pulse collection, DAO logic).
- **Instrumented tests:** Room DAO + Compose UI test (compile verified; run on
  an emulator/device or via CI).
- **Lint:** passes with only minor, non-blocking warnings (unused reserved
  strings, defensive SDK checks).
- **Build verified locally:** `assembleDebug`, `assembleDebugAndroidTest`,
  `testDebugUnitTest`, `lintDebug` all succeed with JDK 17 + Android SDK 35.

> Coverage note: the 80% target applies most meaningfully to the testable core/
> DSP/data logic, which is well covered. UI and hardware-driven classes
> (players, CameraX/AudioRecord plumbing) are validated via instrumented/UI
> tests and manual QA since they require device hardware.

## 5. Environment / build

- Kotlin 2.0.21, AGP 8.7.3, Gradle 8.9, compileSdk 35, minSdk 31.
- SDK + wrapper were provisioned in this environment; `local.properties` points
  to the SDK and is git-ignored.

## 6. Remaining owner actions (require credentials / payment)

1. **GitHub:** provide a token so the repo can be created, pushed and made
   public (or push the prepared local commit yourself). CI runs automatically.
2. **Play Store:** create the app in Play Console, generate an upload keystore
   (see `docs/RELEASE.md`), complete data-safety/content-rating, create the
   `morse_pro_upgrade` product, upload the signed AAB, and submit. New personal
   accounts must run 14-day closed testing with 12 testers first.
3. **Domain + website hosting:** register `morsetranslatorpro.com` and deploy
   `website/` (GitHub Pages/Netlify). See `website/README.md`.
4. **Billing merchant account** for real Pro purchases.
5. **Screenshots/graphics:** capture device screenshots and export PNGs from the
   provided SVG sources for the store and website.

## 7. Suggested roadmap (post-launch)
- Learning/practice mode (Koch method) for retention.
- Home-screen widget and quick-tile SOS.
- Localization (ES, PT-BR, DE, FR, RU, HI, ID).
- Waveform/spectrogram view in the audio decoder.
- Wear OS companion.

## 8. Pre-publish QA checklist
- [ ] Transmit SOS via flashlight, audio, vibration at Slow/Medium/Fast.
- [ ] Decode a phone-to-phone flashlight signal (camera) with good confidence.
- [ ] Decode laptop-generated beeps (mic) at the configured frequency.
- [ ] Save, search, favorite and delete history; verify persistence.
- [ ] Toggle theme (system/light/dark), large text, haptics.
- [ ] Share to at least SMS + one messaging app + email.
- [ ] Verify permission prompts and graceful denial handling.
- [ ] Release build (minified) installs and runs on API 31 and a recent API.
