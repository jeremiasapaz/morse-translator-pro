# Launch Plan

## Phase 1 — Pre-launch (Weeks -3 to 0)

- **Landing page** live at morsetranslatorpro.com with an email waitlist.
- **Closed testing** on Google Play: recruit 12–20 testers (r/amateurradio,
  friends, Discord). Google requires 12 testers for 14 days before production
  access for new personal developer accounts — start this early.
- Collect feedback; fix top issues; capture polished **screenshots** + a short
  **demo video** (flashlight → camera decode).
- Prepare all assets: icon, feature graphic, screenshots, store listing, privacy
  policy URL, data safety answers.
- Draft Product Hunt, Reddit, HN and LinkedIn posts (below).

## Phase 2 — Launch (Week 1)

- **Google Play production** submission (allow 1–7 days review).
- **Product Hunt** launch (Tuesday–Thursday, 12:01am PT). See template below.
- **Social campaign**: posts #1, #8, #18, #6 on day one; then daily cadence.
- **Reddit** posts (value-first, not spammy) in relevant subs, staggered over
  several days to respect each community's self-promo rules.
- **Hacker News** "Show HN" focused on the open-source/technical angle.
- **LinkedIn** announcement from the founder profile + company page.

## Phase 3 — Post-launch (Weeks 2–8)

- **User acquisition:** publish 1 SEO blog post/week; answer Morse/ham questions
  on Reddit/Quora linking contextually; short-form video (TikTok/Reels/Shorts)
  2–3x/week.
- **Reviews:** in-app prompt after a positive moment (successful decode / 3rd
  save); reply to every review; convert feedback to roadmap.
- **Retention:** ship a learning/practice mode and widget; monthly feature
  updates with release notes; optional notification for "Morse word of the day".
- **Measure:** Play Console acquisition reports, store conversion rate, D1/D7/D30
  retention, Pro conversion. Iterate on the first two screenshots (biggest
  conversion lever).

---

## Product Hunt — launch copy

**Tagline (60 chars):** The modern Morse code toolkit — translate, transmit,
decode.

**Description:**
Morse Translator Pro turns your phone into a full Morse station. Convert text to
Morse, transmit it via flashlight, speaker or vibration, and decode flashing
lights or beeps back into text in real time — with a confidence score. It's
fully offline, has no ads or tracking, and is open source (MIT). Built with
Kotlin + Jetpack Compose. We'd love your feedback!

**First comment (maker):** Hi PH! I built this because existing Morse apps were
either ad-heavy, one-trick, or dated. This one does *all four* I/O modes and the
decoding runs entirely on-device. Code is open source — AMA about CameraX,
AudioTrack synthesis, or the Goertzel decoder. 🧡

**Assets:** gallery with 5 screenshots + 30s demo GIF; thumbnail = app icon.

---

## Reddit — launch posts (adapt per sub rules; read each sidebar first)

- **r/amateurradio / r/morsecode:** value-first. "I built a free, offline,
  open-source Morse app that transmits via light/sound/vibration and decodes
  camera + mic — feedback from CW ops welcome." Engage in comments; no hype.
- **r/androidapps / r/androiddev:** lead with tech (Compose, CameraX, DSP) +
  GitHub link. Follow self-promo ratio rules.
- **r/sideproject / r/opensource:** the build story + repo.
- Space posts 2–3 days apart; never cross-post identical text same day.

---

## Hacker News — Show HN

**Title:** Show HN: Morse Translator Pro – offline Morse toolkit (Kotlin,
Compose, on-device DSP)

**Body:** Open-source Android app that encodes/decodes Morse and transmits via
flashlight, audio and vibration. Camera decoding uses CameraX luminance
analysis; audio decoding uses a Goertzel filter with adaptive thresholding. All
processing is on-device/offline. Core logic is Android-free for JVM unit tests.
Repo: [link]. Happy to discuss the DSP and testing approach.

Post ~8–10am ET on a weekday; reply promptly and technically to every comment.

---

## LinkedIn — announcement

🚀 Launching Morse Translator Pro — a modern, open-source Morse code toolkit for
Android.

Translate text ↔ Morse, transmit through your flashlight, speaker or vibration,
and decode flashing lights or beeps back into text in real time — all offline,
no ads, no tracking.

Under the hood: Kotlin, Jetpack Compose, Material 3, CameraX, and real-time DSP
(Goertzel). The encoding and signal-processing core is fully unit-tested on the
JVM.

Free on Google Play, MIT-licensed on GitHub. Links in the comments — feedback
and contributions welcome! #Android #Kotlin #OpenSource #HamRadio
