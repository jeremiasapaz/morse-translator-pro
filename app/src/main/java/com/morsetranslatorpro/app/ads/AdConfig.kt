package com.morsetranslatorpro.app.ads

/**
 * AdMob ad unit IDs.
 *
 * For production, replace these with your actual AdMob ad unit IDs from the AdMob console.
 * Test IDs are used during development to avoid invalid clicks and impressions.
 */
object AdConfig {
    // Test ad IDs (safe for development)
    const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // Production ad unit IDs (replace with your actual IDs before release)
    // const val BANNER_AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
    // const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/ZZZZZZZZZZ"
}
