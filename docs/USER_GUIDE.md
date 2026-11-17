# User Guide

Welcome to **Morse Translator Pro**. This guide walks through every screen.

## Getting started

Open the app and use the ☰ menu (top-left) or the Home grid to move between
tools. Everything works offline — no account or internet needed.

## Text → Morse

1. Type any text in the input field.
2. The Morse code appears instantly below, with a dot/dash visualization.
3. Tap **Copy** or **Share** on the result card, or **Save** to store it in
   History.

Spaces between words are shown as `/`. Unsupported characters appear as `#`.

## Morse → Text

1. Enter Morse using `.` (dot), `-` (dash), a space between letters and `/`
   between words — for example `.... .. / - .... . .-. .`.
2. The decoded text appears on the result card.

## Flashlight Transmitter

1. Enter the message to transmit.
2. Press **Play** — the camera LED blinks the Morse pattern.
3. Use **Pause**, **Stop** and **Repeat** as needed.
4. Adjust speed with the **Slow / Medium / Fast** chips or the WPM slider.

> If your device has no flash, the screen shows a notice and controls are
> disabled.

## Audio Transmitter

Same controls as the flashlight, plus:
- **Tone** slider (400–1200 Hz) to set the beep pitch.
- **Volume** slider.

## Camera Decoder

1. Grant camera permission when asked.
2. Aim the camera at a flashing light (e.g. another phone's flashlight sending
   Morse).
3. The live decoded text, Morse and a **confidence score** update as it reads.
4. Tap **Save** to store the result.

Tips: keep the light centered and steady; avoid very bright backgrounds.

## Audio Decoder

1. Grant microphone permission.
2. Set the expected tone frequency in **Settings** (match the transmitter).
3. Tap **Start listening**. Decoded text appears live with a confidence score.
4. Tap **Stop listening** to finalize.

Tips: works best in a quiet environment; the Goertzel filter rejects broadband
noise but struggles with competing tones at the same frequency.

## History

- Every saved translation is listed newest-first.
- **Search** by text or Morse.
- Tap the ★ to favorite; filter with **Favorites only**.
- Tap the trash icon to delete an entry.

## Settings

- **Theme**: System / Light / Dark (Material You dynamic color on Android 12+).
- **Large text**: scales typography for readability.
- **Haptic feedback**: toggles vibration cues.
- **Pro**: shows entitlement status.

## Sharing

The Copy and Share buttons appear on every result card. Share opens the Android
share sheet so you can send to SMS, WhatsApp, Telegram, Email and more.

## Morse timing reference

| Element       | Length   |
|---------------|----------|
| Dot           | 1 unit   |
| Dash          | 3 units  |
| Symbol gap    | 1 unit   |
| Letter gap    | 3 units  |
| Word gap      | 7 units  |

Speed uses the PARIS standard: `unit (ms) = 1200 / WPM`.
