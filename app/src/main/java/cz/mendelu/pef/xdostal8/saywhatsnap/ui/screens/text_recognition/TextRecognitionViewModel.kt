package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.text_recognition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.core.content.FileProvider
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
) : BaseViewModel(), TextRecognitionActions {

    override fun saveImageProxyToBitmapFile(context: Context, imageProxy: ImageProxy?): Uri? {
        val originalBitmap = imageProxy?.toBitmap()
        val bitmap = rotateBitmap(originalBitmap, 90f) // Rotate the bitmap by 90 degrees
        val filename = "recognized_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.externalMediaDirs.first(), filename)
        try {
            FileOutputStream(file).use { out ->
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    private fun rotateBitmap(source: Bitmap?, angle: Float): Bitmap? {
        if (source == null) return source
        val matrix = Matrix().apply { postRotate(angle) }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

}