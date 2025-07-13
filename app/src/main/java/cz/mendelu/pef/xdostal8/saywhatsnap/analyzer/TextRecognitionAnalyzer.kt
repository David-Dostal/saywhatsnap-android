package cz.mendelu.pef.xdostal8.saywhatsnap.analyzer

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognitionAnalyzer(
    private val onTextDetected: (String, ImageProxy) -> Unit
) : ImageAnalysis.Analyzer {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    // Last time the text was detected
    private var lastAnalyzedTimestamp = 0L

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >= 500) { // 500ms = 0.5 second
            imageProxy.image?.let {
                val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

                textRecognizer.process(image)
                    .addOnSuccessListener { text ->
                        onTextDetected(text.text, imageProxy)
                        lastAnalyzedTimestamp = currentTimestamp
                    }
                    .addOnCompleteListener {
                            imageProxy.close() // Close the imageProxy if the task wasn't successful or no new text was detected

                    }
            }
        } else {
            imageProxy.close() // Make sure to close the imageProxy if not processing
        }
    }
}