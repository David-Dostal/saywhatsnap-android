package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.text_recognition

import android.Manifest
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.analyzer.TextRecognitionAnalyzer
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.CameraView
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.PermissionsRequestDialog
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslateScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.titleLarge


@Destination
@Composable
fun TextRecognitionScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<TextRecognitionViewModel>()
    ScanSurface(navigator = navigator, actions = viewModel)

}


@Composable
fun ScanSurface(
    navigator: DestinationsNavigator, actions: TextRecognitionActions
) {


    PermissionsRequestDialog(
        icon = Icons.Filled.Face,
        title = stringResource(R.string.permissions_camera),
        message = stringResource(R.string.permissions_camera_message),
        permissions = listOf(
            Manifest.permission.CAMERA
        ),
        onPermissionsGranted = {
        }
    )


    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lastImageUri = remember { mutableStateOf<Uri?>(null) }
    val extractedText = remember { mutableStateOf("") }


    var currentImageProxy: ImageProxy? by remember { mutableStateOf(null) }

    CameraView(context = context,
        lifecycleOwner = lifecycleOwner,
        analyzer = TextRecognitionAnalyzer { text, imageProxy ->
            if (text.isNotBlank()) {
                extractedText.value = text
                lastImageUri.value = actions.saveImageProxyToBitmapFile(context, imageProxy)
                currentImageProxy = imageProxy
            }
        })


    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navigator.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = Color.White
                )
            }
            Text(
                text = stringResource(id = R.string.text_recognition_title),
                style = titleLarge(),
                color = Color.White
            )
        }
        // "Extract" Button placed right above the Text box
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Card containing the extracted text
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Text(
                    text = extractedText.value,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Button(onClick = {
                currentImageProxy?.close()  // Close the ImageProxy when stopping the extraction
                navigator.navigate(
                    TranslateScreenDestination(
                        extractedText = extractedText.value, imageUri = lastImageUri.value
                    )
                )
            }) {
                Text(text = stringResource(id = R.string.text_extraction_button))
            }
        }
    }
}

