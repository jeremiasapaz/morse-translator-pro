package com.morsetranslatorpro.app.ui.screens

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.morsetranslatorpro.app.ui.components.ConfidenceBadge
import com.morsetranslatorpro.app.ui.components.ResultCard
import com.morsetranslatorpro.app.ui.components.SignalLevelBar
import com.morsetranslatorpro.app.ui.viewmodel.DecoderViewModel
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraDecodeScreen(viewModel: DecoderViewModel, modifier: Modifier = Modifier) {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val result by viewModel.result.collectAsStateWithLifecycle()
    val level by viewModel.level.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        if (!cameraPermission.status.isGranted) {
            Text("Camera permission is required to decode flashing lights.")
            Button(onClick = { cameraPermission.launchPermissionRequest() }, modifier = Modifier.padding(top = 12.dp)) {
                Text("Grant camera permission")
            }
            return@Column
        }

        Text("Aim the camera at the flashing light source.", style = MaterialTheme.typography.bodyMedium)
        Box(
            modifier = Modifier.fillMaxWidth().height(260.dp).padding(vertical = 12.dp)
        ) {
            CameraPreview(viewModel)
        }
        SignalLevelBar(level)
        ResultCard(
            title = "Decoded text",
            content = result.text,
            modifier = Modifier.padding(top = 16.dp)
        )
        ConfidenceBadge(result.confidence, modifier = Modifier.padding(top = 8.dp))
        Text(result.morse, modifier = Modifier.padding(top = 8.dp))
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { viewModel.saveResult() }, enabled = result.text.isNotBlank()) { Text("Save") }
        }
    }
}

@Composable
private fun CameraPreview(viewModel: DecoderViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val analyzerExecutor = remember { Executors.newSingleThreadExecutor() }
    val decoder = remember { viewModel.newCameraDecoder() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val providerFuture = ProcessCameraProvider.getInstance(ctx)
            providerFuture.addListener({
                val provider = providerFuture.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }
                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { it.setAnalyzer(analyzerExecutor, decoder) }
                provider.unbindAll()
                provider.bindToLifecycle(
                    lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis
                )
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}
