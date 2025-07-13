package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.text_recognition

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageProxy

interface TextRecognitionActions {
    fun saveImageProxyToBitmapFile(context: Context, imageProxy: ImageProxy?): Uri?
}