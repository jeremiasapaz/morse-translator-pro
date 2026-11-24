package com.morsetranslatorpro.app.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Manages AdMob ads (banner and interstitial).
 *
 * Call initialize() once at app startup, then use loadInterstitial() and showInterstitial()
 * for interstitial ads. Banner ads are handled via Compose AdView components.
 */
object AdManager {
    private var interstitialAd: InterstitialAd? = null
    private var isInitialized = false

    /**
     * Initialize AdMob. Call this once at app startup (e.g., in Application.onCreate).
     */
    fun initialize(context: Context) {
        if (isInitialized) return
        com.google.android.gms.ads.MobileAds.initialize(context) { initializationStatus ->
            isInitialized = true
        }
    }

    /**
     * Load an interstitial ad in the background.
     */
    fun loadInterstitial(context: Context) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AdConfig.INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    /**
     * Show the loaded interstitial ad. Returns true if shown, false if not loaded.
     */
    fun showInterstitial(activity: Activity): Boolean {
        val ad = interstitialAd
        if (ad == null) {
            loadInterstitial(activity)
            return false
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                interstitialAd = null
                loadInterstitial(activity)
            }
        }

        ad.show(activity)
        return true
    }

    /**
     * Suspend until an interstitial ad is loaded and shown.
     * Useful for showing ads at specific action points.
     */
    suspend fun showInterstitialSuspend(activity: Activity): Boolean = suspendCancellableCoroutine { continuation ->
        val ad = interstitialAd
        if (ad == null) {
            loadInterstitial(activity)
            continuation.resume(false)
            return@suspendCancellableCoroutine
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(activity)
                continuation.resume(true)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                interstitialAd = null
                loadInterstitial(activity)
                continuation.resume(false)
            }
        }

        ad.show(activity)
    }
}
