package com.morsetranslatorpro.app.playback

import android.content.Context
import android.hardware.camera2.CameraManager

/**
 * Transmits Morse using the device camera flash LED (torch).
 */
class FlashlightPlayer(context: Context) : AbstractElementPlayer() {

    private val cameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private val cameraId: String? = runCatching {
        cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
    }.getOrNull()

    /** True if this device exposes a controllable flash unit. */
    val isAvailable: Boolean get() = cameraId != null

    override fun onSignal(on: Boolean) {
        val id = cameraId ?: return
        runCatching { cameraManager.setTorchMode(id, on) }
    }
}
