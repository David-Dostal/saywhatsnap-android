@file:OptIn(ExperimentalMaterial3Api::class)

package  cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationEditScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.basicMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.getCurrentPrimaryColor
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.roundedCorner
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.smallMargin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomSheet(
    isVisible: Boolean,
    translation: TranslationEntity,
    onDismiss: () -> Unit,
    navigator: DestinationsNavigator
) {


    ModalBottomSheet(
        modifier = Modifier,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        ),
        onDismissRequest = onDismiss,

        shape = RoundedCornerShape(
            topStart = 10.dp,
            topEnd = 10.dp
        ),
        dragHandle = {
            Spacer(
                modifier = Modifier
                    .width(30.dp)
                    .height(5.dp)
                    .background(getCurrentPrimaryColor())
            )
        },
    ) {
        if (isVisible) {

            CustomBottomSheetContainer(translation = translation, navigator = navigator)
        }
    }
}


@Composable
fun CustomBottomSheetContainer(translation: TranslationEntity, navigator: DestinationsNavigator) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = basicMargin(), vertical = basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = translation.name,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = translation.category,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(basicMargin()))

            val imagePainter = if (translation.image.isEmpty()) {
                painterResource(id = R.drawable.translation_placeholder)
            } else {
                rememberImagePainter(data = translation.image)
            }
            Image(
                painter = imagePainter,
                contentDescription = translation.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(basicMargin()))


            Text(
                text = translation.originalString,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = translation.originalLanguage,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(smallMargin()))

            Image(
                painter = rememberImagePainter(R.drawable.translation_arrow),
                contentDescription = stringResource(id = R.string.translation_image),
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .rotate(90F),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(smallMargin()))

            Text(
                text = translation.translatedString,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = translation.translatedLanguage,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(basicMargin()))

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(getCurrentPrimaryColor()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = translation.description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(basicMargin())
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(basicMargin()))
            Spacer(modifier = Modifier.height(basicMargin()))


            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navigator.navigate(TranslationEditScreenDestination(translationID = translation.id!!))
                },
                shape = RoundedCornerShape(roundedCorner()),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White, containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(R.string.button_edit_translation))
            }


            Spacer(modifier = Modifier.height(basicMargin()))
            Spacer(modifier = Modifier.height(basicMargin()))
        }
    }
}
