# Play Console — Data Safety Form Answers

Use these answers when completing the Data Safety section in Play Console.

## Data collection and sharing
- **Does your app collect or share any of the required user data types?** Yes —
  via Google AdMob for advertising purposes only.

The app processes all core data on-device. Camera frames and microphone audio are
analyzed in memory in real time and never stored or transmitted. Saved
translation history and settings remain local to the device.

Google AdMob may collect the following for ad serving and measurement:
- Device identifiers (e.g., advertising ID)
- App activity and usage data
- Approximate location (general area, not precise)

This data is collected and used by Google under their privacy policy, not by us.

## Security practices
- **Is all user data encrypted in transit?** Not applicable — no user data is
  transmitted off the device.
- **Do you provide a way for users to request data deletion?** Yes — users can
  delete saved history in-app (History screen → delete / clear), and uninstalling
  removes all local data.

## Data types — detailed
| Data type | Collected | Shared | Purpose |
|-----------|-----------|--------|---------|
| Personal info | No | No | — |
| Location | Yes (approximate, via AdMob) | No | Ad targeting and measurement (Google) |
| Photos/Camera | Processed on-device, not collected | No | Real-time light decoding only |
| Audio/Microphone | Processed on-device, not collected | No | Real-time beep decoding only |
| App activity (history) | Stored locally only; usage data via AdMob | No | User-saved translations; ad measurement (Google) |
| Purchases | Handled by Google Play Billing | No | Pro upgrade |

## Permissions declared
- CAMERA — camera Morse decoding
- RECORD_AUDIO — microphone Morse decoding
- VIBRATE — vibration transmission
- com.android.vending.BILLING — Pro upgrade

## Ads
Yes — the app displays banner and interstitial ads via Google AdMob. AdMob may
collect device identifiers, app usage, and approximate location for ad targeting
and measurement per Google's privacy policy. Users can opt out of personalized
ads via Google Ad Settings (https://adssettings.google.com).

## Families / Target audience
Target audience: 13+. Not primarily directed at children. Content rating:
Everyone.
