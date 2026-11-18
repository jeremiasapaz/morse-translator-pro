# Screenshots

These PNGs are **generated automatically** from the real app UI using Robolectric
+ Roborazzi (JVM rendering — no device/emulator needed). They are 1080×2340
(Pixel 5) and ready to upload to Google Play (which needs 2–8 phone shots).

## Regenerate

```bash
./gradlew recordRoborazziDebug -Pscreenshots
```

Output is written here by `app/src/test/java/com/morsetranslatorpro/app/screenshots/ScreenshotTest.kt`.

> On a corporate Windows machine behind a TLS-intercepting proxy, Robolectric's
> first run must download its runtime jars. The build passes
> `-Djavax.net.ssl.trustStoreType=WINDOWS-ROOT` to the test JVM automatically on
> Windows. If the Gradle daemon itself can't resolve the robolectric native
> runtime, temporarily add that same flag to `org.gradle.jvmargs` for one run.

## Current set
| File | Screen |
|------|--------|
| 01-home.png | Home feature grid (light) |
| 02-text-to-morse.png | Text → Morse with dot/dash visualization |
| 03-morse-to-text.png | Morse → Text decoding |
| 04-flashlight.png | Flashlight transmitter |
| 05-audio.png | Audio transmitter (tone/volume/speed) |
| 06-history.png | History with favorites |
| 07-settings-light.png | Settings (light) |
| 08-settings-dark.png | Settings (dark) |
| 09-about.png | About |
| 10-home-dark.png | Home feature grid (dark) |

## Recommended Play Store order (max 8)
02, 04, 06, 10, 05, 03, 08, 01 — lead with the clearest value props
(Text→Morse result and a transmitter), include a dark-mode shot.

## Live decoder screens
The camera/mic decoder screens need real hardware, so they aren't auto-captured.
Capture those on a device with:
```bash
adb exec-out screencap -p > camera-decoder.png
```
