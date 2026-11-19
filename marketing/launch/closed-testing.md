# Closed Testing Kit

Google Play requires **new personal developer accounts** to run a closed test
with **at least 12 testers opted in for 14 continuous days** before you can
apply for production access. This kit helps you hit that bar quickly.

Repo: https://github.com/jeremiasapaz/morse-translator-pro
Site: https://jeremiasapaz.github.io/morse-translator-pro/

---

## 1. Set up the closed test in Play Console

1. Play Console → your app → **Testing → Closed testing → Create track**
   (name it e.g. "closed-alpha").
2. Create an **email list** of testers (Testers tab → Create email list) and add
   all tester Google account emails (the Gmail they use on their phone).
3. Upload the AAB (`release-artifacts/morse-translator-pro-v1.0.0.aab`) to the
   track, add release notes (below), review and **roll out**.
4. Copy the **opt-in URL** Play gives you and send it to testers (template below).
5. Each tester must **tap the opt-in link, accept, then install from Play**.
   Only testers who install and stay opted in count toward the 12/14-day rule.

> Keep the track live and the build installed for 14 continuous days. Don't
> remove testers mid-window.

## 2. Where to recruit 12+ testers (ethically)

- Friends & family with Android phones (fastest 5–8).
- r/amateurradio, r/morsecode, r/androidapps "beta testers wanted" threads
  (follow each sub's rules; be transparent it's a closed test).
- Discord/Telegram ham-radio and Android communities.
- The official Google Play beta-testing exchange communities
  (e.g. r/TestMyApp, "12 testers" Google Groups) — reciprocate by testing theirs.
- Work/study groups, maker/scout groups.

Aim for ~15 to safely clear the 12 minimum (some drop off).

## 3. Tester tracking sheet (copy to a spreadsheet)

| # | Name | Google email | Invited (date) | Opted in | Installed | Device / Android | Notes / feedback |
|---|------|--------------|----------------|----------|-----------|------------------|------------------|
| 1 |      |              |                | ☐        | ☐         |                  |                  |
| 2 |      |              |                | ☐        | ☐         |                  |                  |
| … | …    | …            | …              | ☐        | ☐         | …                | …                |
| 15|      |              |                | ☐        | ☐         |                  |                  |

Target: **≥12 "Installed" = ✓** sustained for 14 days.

## 4. Release notes (first closed build)

```
v1.0.0 (closed test)
Thanks for testing Morse Translator Pro! This build includes:
• Text ↔ Morse translation
• Transmit via flashlight, audio and vibration
• Decode flashing lights (camera) and beeps (microphone)
• History, sharing, dark mode and accessibility options
Please try each feature and send feedback. Known limits: live decoding accuracy
depends on lighting/noise. Thank you! 🔦🔊
```

## 5. What to ask testers to check (QA focus)

- Translate a few phrases both directions.
- Transmit SOS via flashlight, audio and vibration at Slow/Medium/Fast.
- Decode a flashlight signal from a second phone (camera).
- Decode laptop-generated beeps (microphone) — set matching tone in Settings.
- Save/search/favorite history; share to a messaging app.
- Toggle dark mode + large text.
- Report device model, Android version, and anything confusing or broken.

## 6. Feedback intake

- Email: support@morsetranslatorpro.com
- Or a short Google Form (3 questions: what worked, what broke, device/OS).
- Triage into GitHub issues: https://github.com/jeremiasapaz/morse-translator-pro/issues
