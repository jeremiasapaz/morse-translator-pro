# Play Console — Data Safety Form Answers

Use these answers when completing the Data Safety section in Play Console.

## Data collection and sharing
- **Does your app collect or share any of the required user data types?** No.

The app processes all data on-device. Camera frames and microphone audio are
analyzed in memory in real time and never stored or transmitted. Saved
translation history and settings remain local to the device.

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
| Location | No | No | — |
| Photos/Camera | Processed on-device, not collected | No | Real-time light decoding only |
| Audio/Microphone | Processed on-device, not collected | No | Real-time beep decoding only |
| App activity (history) | Stored locally only | No | User-saved translations |
| Purchases | Handled by Google Play Billing | No | Pro upgrade |

## Permissions declared
- CAMERA — camera Morse decoding
- RECORD_AUDIO — microphone Morse decoding
- VIBRATE — vibration transmission
- com.android.vending.BILLING — Pro upgrade

## Ads
No ads. No advertising SDKs.

## Families / Target audience
Target audience: 13+. Not primarily directed at children. Content rating:
Everyone.
