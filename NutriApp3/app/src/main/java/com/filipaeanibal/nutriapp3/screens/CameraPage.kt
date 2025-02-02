package com.filipaeanibal.nutriapp3.screens


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.camera.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import com.filipaeanibal.nutriapp3.models.FoodDetector.OnnxModel
import com.filipaeanibal.nutriapp3.models.FoodDetector.getLabelFromOutput
import com.filipaeanibal.nutriapp3.models.FoodDetector.calculateBoundingBox
import com.filipaeanibal.nutriapp3.util.FoodDetector.toBitmap
import com.filipaeanibal.nutriapp3.util.FoodDetector.preprocessBitmap
import java.io.File
import androidx.navigation.NavHostController


@Composable
fun CameraPage(
    onNavigate: (String) -> Unit, )
{
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var detectedFood by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasCameraPermission = isGranted }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            hasCameraPermission = true
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (capturedBitmap == null) {
                CameraPreview(
                    onImageCaptured = { bitmap ->
                        capturedBitmap = bitmap
                        try {
                            processCapturedImage(bitmap, context) { food, _ ->
                                detectedFood = food
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error processing image: ${e.localizedMessage}"
                            e.printStackTrace()
                        }
                    }
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    capturedBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    }
                    detectedFood?.let { food ->
                        Text(
                            text = "Detected: $food",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Button(
                        onClick = {onNavigate("pesquisarAlimentos") },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Return")
                    }
                }

            }
        }
    } else {
        Text("Camera permission is required to continue")
    }
}

@Composable
fun CameraPreview(onImageCaptured: (Bitmap) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            val previewView = PreviewView(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                preview.setSurfaceProvider(previewView.surfaceProvider)
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = {
                val file = File(context.cacheDir, "captured_image.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            onImageCaptured(bitmap)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            exception.printStackTrace()
                        }
                    }
                )
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Capture Photo")
        }
    }
}

fun processCapturedImage(
    bitmap: Bitmap,
    context: Context,
    onResult: (String, BoundingBox?) -> Unit
) {
    val model = OnnxModel(context)
    try {
        val inputData = preprocessBitmap(bitmap)
        val inputTensor = model.prepareInput(inputData, longArrayOf(1, 3, 224, 224))
        val output = model.runInference(inputTensor)

        val detectedFood = getLabelFromOutput(output)
        val boundingBox = calculateBoundingBox(output)

        println("Modelo detectou: $detectedFood")
        println("Bounding Box: $boundingBox")

        onResult(detectedFood, boundingBox)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

data class BoundingBox(val left: Float, val top: Float, val right: Float, val bottom: Float)

@Composable
fun DrawBoundingBox(boundingBox: BoundingBox) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            color = Color.Red,
            topLeft = androidx.compose.ui.geometry.Offset(boundingBox.left, boundingBox.top),
            size = androidx.compose.ui.geometry.Size(
                width = boundingBox.right - boundingBox.left,
                height = boundingBox.bottom - boundingBox.top
            ),
            style = Stroke(width = 4f)
        )
    }
}
fun saveDetectedFood(food: String?, bitmap: Bitmap?, context: Context) {
    if (food != null && bitmap != null) {
        println("Alimento guardado: $food")
    } else {
        println("Nada para guardar.")
    }
}


//Nao foi possivel implementar a detecao de alimentos....