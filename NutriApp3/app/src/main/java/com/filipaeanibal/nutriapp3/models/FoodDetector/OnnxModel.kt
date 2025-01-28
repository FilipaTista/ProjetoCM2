package com.filipaeanibal.nutriapp3.models.FoodDetector


import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OrtSession.SessionOptions
import android.content.Context
import java.nio.FloatBuffer


class OnnxModel(context: Context) {
    private val ortEnvironment = OrtEnvironment.getEnvironment()
    private lateinit var ortSession: OrtSession

    init {

        val modelPath = "food_detector.onnx"
        context.assets.open(modelPath).use { inputStream ->
            val modelBytes = inputStream.readBytes()
            ortSession = ortEnvironment.createSession(modelBytes, SessionOptions())
        }
    }

    fun runInference(inputTensor: OnnxTensor): FloatArray {
        val inputs = mapOf("input" to inputTensor)
        val results: OrtSession.Result = ortSession.run(inputs)
        val outputTensor = results.get("output") as OnnxTensor

        // Converter o tensor de saída para um array de float
        return outputTensor.floatBuffer.array()
    }

    fun prepareInput(inputData: FloatArray, shape: LongArray): OnnxTensor {
        return OnnxTensor.createTensor(ortEnvironment, FloatBuffer.wrap(inputData), shape)
    }
}


