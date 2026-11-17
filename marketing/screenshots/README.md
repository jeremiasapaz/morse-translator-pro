# Screenshots

Capture these on a device/emulator (API 31+) and place the PNGs here. Google
Play requires 2–8 phone screenshots, 16:9 or 9:16, min 320px, max 3840px side.

## Recommended set (in order — first two drive conversion)
1. **Text → Morse** result with dot/dash visualization.
2. **Camera Decoder** showing live decoded text + confidence score.
3. **Flashlight Transmitter** with the big signal indicator mid-flash.
4. **Audio Transmitter** with tone/volume/speed controls.
5. **Audio Decoder** listening with the level meter.
6. **History** with favorites.
7. **Settings** showing dark mode + Material You.

## How to capture
```bash
# with a device/emulator connected
adb exec-out screencap -p > 01-text-to-morse.png
```
Or use Android Studio's Running Devices > camera icon.

## Optional: framed marketing screenshots
Add device frames and a caption banner (use the brand palette) for a polished
store gallery. Keep captions ≤ 5 words, e.g. "Decode flashing lights."
