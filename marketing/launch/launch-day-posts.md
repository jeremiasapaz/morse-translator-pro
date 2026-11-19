# Launch-Day Posts (ready to copy/paste)

Fill in `[PLAY_URL]` once the app is live on Google Play. The GitHub and website
links below are live now.

- GitHub: https://github.com/jeremiasapaz/morse-translator-pro
- Website: https://jeremiasapaz.github.io/morse-translator-pro/

Best timing: Tue–Thu. Product Hunt at 12:01am PT; Hacker News ~8–10am ET.

---

## Product Hunt

**Name:** Morse Translator Pro
**Tagline:** The modern Morse code toolkit — translate, transmit, decode.
**Topics:** Android, Open Source, Productivity, Developer Tools
**Links:** Website + GitHub + [PLAY_URL]

**Description:**
Morse Translator Pro turns your phone into a full Morse station. Convert text to
Morse, transmit it via flashlight, speaker or vibration, and decode flashing
lights or beeps back into text in real time — with a confidence score. It's
100% offline, has no ads or tracking, and is open source (MIT). Built with
Kotlin + Jetpack Compose.

**First comment (maker):**
Hi PH! 👋 I built this because the Morse apps I found were either ad-heavy,
single-purpose, or looked dated. This one does all four I/O modes — flashlight,
audio, vibration out; camera + mic in — and every bit of processing runs
on-device. The encoding and the signal-processing (Goertzel tone detection,
adaptive thresholding) are open source and unit-tested on the JVM. Happy to go
deep on the DSP, CameraX, or AudioTrack synthesis — AMA! 🧡

---

## Hacker News — Show HN

**Title:** Show HN: Morse Translator Pro – offline Morse toolkit (Kotlin, Compose, on-device DSP)

**URL:** https://github.com/jeremiasapaz/morse-translator-pro

**Text:**
I built an open-source Android app that encodes/decodes Morse and transmits via
flashlight, audio and vibration. Camera decoding uses CameraX luminance
analysis; audio decoding uses a Goertzel filter with adaptive thresholding to
reject background noise. Everything runs on-device/offline.

I kept the Morse engine and the signal-processing core free of Android
dependencies so they're unit-tested on the JVM (and I render the store
screenshots headlessly with Robolectric + Roborazzi). Stack: Kotlin, Jetpack
Compose, Material 3, Room, CameraX, Play Billing.

Repo: https://github.com/jeremiasapaz/morse-translator-pro
Play: [PLAY_URL]
Happy to discuss the DSP, the testing approach, or the Compose architecture.

---

## Reddit

> Read each subreddit's self-promotion rules first; space posts 2–3 days apart;
> lead with value and engage in comments.

**r/amateurradio** (and r/morsecode)
Title: I built a free, offline, open-source Morse app — transmit by light/sound/vibration, decode by camera/mic
Body: Fellow CW folks — I made Morse Translator Pro to practice and signal. It
sends Morse via flashlight, tone (adjustable 400–1200 Hz) or vibration, and
decodes flashing lights (camera) or beeps (mic) live with a confidence score.
WPM is adjustable 1–60 (PARIS). No ads, no account, fully offline, MIT-licensed.
Would love feedback from experienced ops on decoding accuracy and the keying
timing. Repo: [link] · Play: [PLAY_URL]

**r/androidapps**
Title: [Free] Morse Translator Pro — translate, flash, beep & decode Morse, offline & no ads
Body: Material You UI, dark mode, Room history, sharing. Free core; one-time Pro
upgrade (no ads ever). Open source. Screenshots + APK via GitHub. Feedback
welcome!

**r/androiddev**
Title: Open-source Morse app: Jetpack Compose + CameraX + on-device DSP (Goertzel), JVM-tested core
Body: Sharing the codebase as a compact real-world example — Compose + Material 3,
MVVM, Room/DataStore, CameraX ImageAnalysis for light decoding, AudioRecord +
Goertzel for beep decoding, AudioTrack sine synthesis for output. Core logic is
Android-free and unit-tested; screenshots generated via Roborazzi. Repo: [link]

**r/sideproject / r/opensource**
Title: I shipped Morse Translator Pro — my open-source, offline Morse toolkit for Android
Body: Build story + what I learned (the DSP and keeping logic testable). Repo +
site in post. Stars and PRs welcome ⭐

---

## LinkedIn

🚀 Launching Morse Translator Pro — a modern, open-source Morse code toolkit for
Android.

Translate text ↔ Morse, transmit through your flashlight, speaker or vibration,
and decode flashing lights or beeps back into text in real time — all offline,
no ads, no tracking.

Under the hood: Kotlin, Jetpack Compose, Material 3, CameraX, and real-time DSP
(Goertzel tone detection + adaptive thresholding). The encoding and
signal-processing core is Android-free and fully unit-tested on the JVM, with CI
running tests, lint and emulator checks on every push.

Free on Google Play, MIT-licensed on GitHub.
🔗 GitHub: https://github.com/jeremiasapaz/morse-translator-pro
🔗 Site: https://jeremiasapaz.github.io/morse-translator-pro/
🔗 Play: [PLAY_URL]

#Android #Kotlin #JetpackCompose #OpenSource #HamRadio #MobileDevelopment

---

## X / Twitter thread

1/ We shipped 🚀 Morse Translator Pro — turn your phone into a full Morse
station. Translate, transmit (flashlight/audio/vibration) and decode
(camera/mic). Offline. Open source. [PLAY_URL]

2/ Point your camera at a flashing light and watch it become text — live, with a
confidence score. 🔦➡️🔤

3/ Dev bits: on-device DSP (Goertzel + adaptive threshold), CameraX luminance
analysis, AudioTrack sine synthesis. Core logic is Android-free & JVM-tested.
Code: https://github.com/jeremiasapaz/morse-translator-pro

4/ Free, no ads, no tracking. Optional one-time Pro upgrade. Built with Kotlin +
Jetpack Compose + Material 3. Feedback welcome 🧡

---

## Launch-day checklist
- [ ] App approved & live on Play; paste [PLAY_URL] everywhere above.
- [ ] Website shows the correct Play badge/link.
- [ ] Product Hunt scheduled (12:01am PT) + first maker comment ready.
- [ ] Show HN posted (~8–10am ET); reply to every comment.
- [ ] Reddit posts staggered; LinkedIn + X thread live.
- [ ] Monitor Play Console reviews/crashes; respond fast.
