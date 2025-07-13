package cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import cz.mendelu.pef.xdostal8.saywhatsnap.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsRequestDialog(
    title: String,
    message: String,
    permissions: List<String>,
    onPermissionsGranted: () -> Unit,
    icon: ImageVector
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)
    val locationFetched = remember { mutableStateOf(false) }

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            onPermissionsGranted()
            locationFetched.value = true
        }
    }

    if (!permissionsState.allPermissionsGranted) {
        Dialog(onDismissRequest = { }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.permission_icon),
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = message,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { permissionsState.launchMultiplePermissionRequest() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.accept))
                    }
                }
            }
        }
    }
}
