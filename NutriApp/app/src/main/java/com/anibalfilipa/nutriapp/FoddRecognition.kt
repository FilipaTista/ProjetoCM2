package com.anibalfilipa.nutriapp

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.LabelUtil
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class FoodRecognition(context: Context) {

    private var interpreter: Interpreter? = null

    init {
        // Carregar o modelo .tflite
        val model = FileUtil.loadMappedFile(context, "food_model.tflite")
        interpreter = Interpreter(model)
    }

    fun recognizeFood(image: Bitmap): List<Category> {
        // Converte a imagem para o formato que o modelo possa entender
        val tensorImage = TensorImage.fromBitmap(image)

        // Definir o formato de saída do modelo
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 101), DataType.FLOAT32)

        // Executar a inferência
        interpreter?.run(tensorImage.buffer, outputBuffer.buffer.rewind())

        // Decode a saída do modelo
        return LabelUtil.decodeOutput(outputBuffer)
    }
}
