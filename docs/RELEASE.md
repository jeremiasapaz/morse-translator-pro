# Release & Publishing Guide

This document describes how to produce a signed release build and publish to the
Google Play Store.

## 1. Signing configuration

Signing secrets are **never** committed. Create a keystore and a
`keystore.properties` file locally (both are git-ignored).

```bash
keytool -genkey -v -keystore morse-release.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias morse
```

Create `keystore.properties` in the project root:

```properties
storeFile=../morse-release.jks
storePassword=********
keyAlias=morse
keyPassword=********
```

Add the following to `app/build.gradle.kts` (kept out of the repo until you have
a keystore so CI builds don't fail):

```kotlin
import java.util.Properties

val keystorePropsFile = rootProject.file("keystore.properties")
val keystoreProps = Properties().apply {
    if (keystorePropsFile.exists()) load(keystorePropsFile.inputStream())
}

android {
    signingConfigs {
        create("release") {
            if (keystorePropsFile.exists()) {
                storeFile = file(keystoreProps["storeFile"] as String)
                storePassword = keystoreProps["storePassword"] as String
                keyAlias = keystoreProps["keyAlias"] as String
                keyPassword = keystoreProps["keyPassword"] as String
            }
        }
    }
    buildTypes {
        getByName("release") {
            if (keystorePropsFile.exists()) signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

## 2. Build the release artifacts

```bash
# Android App Bundle (what Play Store wants)
./gradlew bundleRelease
# Output: app/build/outputs/bundle/release/app-release.aab

# Optional: a signed APK for sideloading / direct testing
./gradlew assembleRelease
```

## 3. Google Play Console steps (manual, needs your developer account)

1. Create the app in [Play Console](https://play.google.com/console):
   - App name: **Morse Translator Pro**
   - Default language: English (US)
   - App or game: App; Free with in-app purchases; Ad-supported.
2. Complete **App content**: privacy policy URL, data safety (see
   `marketing/play-store/data-safety.md`), ads (none), content rating
   questionnaire (expected: Everyone), target audience, news (no).
3. Set up the **Pro** in-app product:
   - Product ID: `morse_pro_upgrade` (must match `BillingManager.PRO_PRODUCT_ID`)
   - Type: one-time product.
4. **Store listing**: copy text from `marketing/play-store/listing.md`; upload
   the icon, feature graphic and screenshots from `marketing/`.
5. Upload the AAB to a **Closed testing** track first; run through the testing
   checklist in `docs/PROJECT_REPORT.md`.
6. Promote to **Production** and submit for review.

## 4. Play App Signing

Enroll in Play App Signing (recommended). Google manages the app signing key;
your upload key is the keystore created above. Keep the upload key safe — losing
it requires an upload-key reset with Google support.

## 5. CI releases (optional)

The GitHub Actions workflow builds and tests on every push. To automate
Play uploads, add a `fastlane` or `r0adkll/upload-google-play` step using a Play
service-account JSON stored as an encrypted GitHub secret
(`PLAY_SERVICE_ACCOUNT_JSON`) and the keystore as base64 secrets. See the
commented block in `.github/workflows/android.yml`.
