package com.morsetranslatorpro.app.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.morsetranslatorpro.app.data.prefs.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Wraps Google Play Billing for the one-time "Pro" upgrade. Entitlement is
 * persisted to [SettingsRepository] so the rest of the app only depends on the
 * `isPro` flag. Fails gracefully on devices without Play services.
 */
class BillingManager(
    context: Context,
    private val settings: SettingsRepository
) : PurchasesUpdatedListener {

    companion object {
        const val PRO_PRODUCT_ID = "morse_pro_upgrade"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var proProductDetails: ProductDetails? = null

    private val client: BillingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    fun connect() {
        if (client.isReady) return
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProduct()
                    refreshPurchases()
                }
            }
            override fun onBillingServiceDisconnected() { /* retried on next connect() */ }
        })
    }

    private fun queryProduct() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRO_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()
        client.queryProductDetailsAsync(params) { result, details ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                proProductDetails = details.firstOrNull()
            }
        }
    }

    /** Launches the purchase flow for the Pro upgrade. */
    fun purchasePro(activity: Activity) {
        val details = proProductDetails ?: return
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(details)
                        .build()
                )
            ).build()
        client.launchBillingFlow(activity, params)
    }

    /** Re-checks existing purchases (e.g. after reinstall) and updates entitlement. */
    fun refreshPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        client.queryPurchasesAsync(params) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                val owned = purchases.any {
                    it.products.contains(PRO_PRODUCT_ID) &&
                        it.purchaseState == Purchase.PurchaseState.PURCHASED
                }
                scope.launch { settings.setPro(owned) }
            }
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode != BillingClient.BillingResponseCode.OK || purchases == null) return
        for (purchase in purchases) {
            if (purchase.products.contains(PRO_PRODUCT_ID) &&
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            ) {
                scope.launch { settings.setPro(true) }
                if (!purchase.isAcknowledged) {
                    val ack = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken).build()
                    client.acknowledgePurchase(ack) { }
                }
            }
        }
    }

    val proPrice: String? get() = proProductDetails?.oneTimePurchaseOfferDetails?.formattedPrice
}
