package com.morsetranslatorpro.app.ads

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * Compose wrapper for AdMob banner ads.
 *
 * @param modifier Modifier for the ad view
 * @param adSize Ad size (default: adaptive banner)
 */
@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adSize: AdSize = AdSize.BANNER
) {
    val context = LocalContext.current
    val adView = remember {
        AdView(context).apply {
            setAdSize(adSize)
            adUnitId = AdConfig.BANNER_AD_UNIT_ID
        }
    }

    AndroidView(
        factory = {
            adView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { it },
        modifier = modifier
    )
}
