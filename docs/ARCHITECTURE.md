# Architecture

Morse Translator Pro follows a layered **MVVM** architecture with a strict
separation between pure domain logic and Android platform code.

## Layers

### 1. Core domain (`com.morsetranslatorpro.app.core`)

Pure Kotlin, no Android dependencies, 100% unit-testable:

- **`MorseCode`** – the ITU international table and `encode`/`decode` functions.
- **`MorseTiming`** – converts a Morse string into a list of timed on/off
  `MorseElement`s using the PARIS WPM standard (unit ms = 1200 / WPM).
- **`SignalDecoder`** – turns a stream of detected `Pulse`s into Morse and text,
  estimating the unit length adaptively and reporting a confidence score.

### 2. DSP / decoding (`com.morsetranslatorpro.app.decoder`)

- **`Goertzel`** – single-frequency energy detector for audio tone detection.
- **`AdaptiveThreshold`** – running floor/peak tracker to classify on/off for
  both audio magnitude and camera luminance.
- **`PulseCollector`** – debounces boolean samples into discrete pulses.
- **`CameraDecoder`** – a CameraX `ImageAnalysis.Analyzer` measuring central
  luminance.
- **`AudioDecoder`** – records PCM and runs Goertzel + threshold in real time.

### 3. Playback (`com.morsetranslatorpro.app.playback`)

- **`MorsePlayer`** interface + `PlaybackState`.
- **`AbstractElementPlayer`** – coroutine-driven timing loop with
  pause/resume/stop and progress; used by flashlight and vibration.
- **`FlashlightPlayer`** (Camera2 torch), **`VibrationPlayer`** (VibrationEffect
  waveform), **`AudioPlayer`** (streaming `AudioTrack` sine synthesis).

### 4. Data (`com.morsetranslatorpro.app.data`)

- **Room** – `HistoryEntity`, `HistoryDao`, `MorseDatabase` for persistent
  history with search and favorites.
- **DataStore** – `SettingsRepository` exposes an `AppSettings` flow + setters.
- **`HistoryRepository`** – mediates history access and the free-tier limit.

### 5. Billing (`com.morsetranslatorpro.app.billing`)

- **`BillingManager`** – Google Play Billing wrapper for the one-time Pro
  upgrade; entitlement persisted via `SettingsRepository.isPro`.

### 6. UI (`com.morsetranslatorpro.app.ui`)

- Compose screens under `ui/screens`, reusable widgets under `ui/components`,
  theme under `ui/theme`, navigation under `ui/navigation`.
- **ViewModels** (`ui/viewmodel`) expose `StateFlow` state; built via a manual
  `ViewModelFactory` fed from `AppContainer` (lightweight DI).

## Data flow

```
User input ─▶ ViewModel (StateFlow) ─▶ Compose recomposition
                     │
                     ├─▶ core.MorseCode / MorseTiming (encode)
                     ├─▶ playback.* (transmit)
                     └─▶ data.* (persist history / settings)

Sensors ─▶ decoder.* (CameraDecoder / AudioDecoder)
                     │  Pulse stream
                     └─▶ core.SignalDecoder ─▶ DecodeResult ─▶ ViewModel ─▶ UI
```

## Module / dependency diagram

```
        ┌─────────────┐
        │     ui      │  (Compose, ViewModels)
        └──────┬──────┘
               │ depends on
     ┌─────────┼───────────────┬───────────────┐
     ▼         ▼               ▼               ▼
  playback  decoder          data           billing
     │         │               │
     └────┬────┘               │
          ▼                    │
        core  ◀────────────────┘ (data uses core types)
```

`core` has **no** dependencies on any other module/package, which is what keeps
it unit-testable on the JVM.

## Threading

- Playback and decoding run on `Dispatchers.Default` / dedicated executors.
- Camera analysis runs on a single-thread executor; UI state is updated via
  `StateFlow` which Compose collects with lifecycle awareness.
- All Room/DataStore access is from `viewModelScope` coroutines.

## Why manual DI?

The app is small enough that a full DI framework (Hilt) would add build
complexity without much benefit. `AppContainer` + `ViewModelFactory` provide
singletons and constructor injection with negligible overhead, and remain easy
to swap for Hilt later if the project grows.
