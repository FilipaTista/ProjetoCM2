package com.filipaeanibal.nutriapp3.util.FoodDetector


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.ImageProxy

fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
fun preprocessBitmap(bitmap: Bitmap): FloatArray {
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 160, 160, true)

    val floatArray = FloatArray(3 * 160 * 160)
    var index = 0
    for (y in 0 until 160) {
        for (x in 0 until 160) {
            val pixel = resizedBitmap.getPixel(x, y)
            floatArray[index++] = (pixel shr 16 and 0xFF) / 255.0f
            floatArray[index++] = (pixel shr 8 and 0xFF) / 255.0f
            floatArray[index++] = (pixel and 0xFF) / 255.0f
        }
    }
    return floatArray
}