package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_list

import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyBaseScreen
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.SelectedDateField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.SettingsScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationEditScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.basicMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.smallestMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.utils.DateUtils
import java.util.Calendar

const val TestTagDateField = "TestTagDateField"
const val TestTagSettingsButton = "TestTagSettingsButton"

@RootNavGraph(start = true)
@Destination
@Composable
fun TranslationListScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<TranslationListViewModel>()

    val uiData: TranslationListData by remember { mutableStateOf(viewModel.uiData) }

    val transUIState by remember { mutableStateOf(viewModel.transUIState) }

    val selectedDateCalendar = Calendar.getInstance().apply {
        timeInMillis = uiData.date
    }
    val selectedYear = selectedDateCalendar.get(Calendar.YEAR)
    val selectedMonth = selectedDateCalendar.get(Calendar.MONTH)
    val selectedDay = selectedDateCalendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        LocalContext.current, { _: DatePicker, year: Int, month: Int, day: Int ->
            viewModel.changeSelectedDate(DateUtils.getUnixTime(year, month, day))
        }, selectedYear, selectedMonth, selectedDay
    )





    MyBaseScreen(
        showLoading = transUIState.value.loading,
        customTitle = {
            SelectedDateField(
                value = viewModel.uiData.date.let { DateUtils.getDateString(it) },
                onClick = { datePickerDialog.show() },
                modifier = Modifier.testTag(TestTagDateField)
            )
        },
        drawFullScreenContent = true,
        placeholderScreenContent = if (transUIState.value.data?.isEmpty() == true) {
            PlaceholderScreenContent(
                image = R.drawable.placeholder_no_tasks,
                text = stringResource(R.string.no_translations_text)
            )
        } else
            null,
        currentDestinationPosition = 0,
        navigator = navigator,
        actions = {
            FilledIconButton(
                onClick = {
                    navigator.navigate(SettingsScreenDestination)

                },
                modifier = Modifier.testTag("TestTagSettingsButton")
            ) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.settings_description)
                )
            }
        })
    {
        TranslationListScreenContent(
            paddingValues = it,
            translations = transUIState.value.data,
            navigator = navigator,
        )
    }


}

@Composable
fun TranslationListScreenContent(
    paddingValues: PaddingValues,
    translations: List<TranslationEntity>?,
    navigator: DestinationsNavigator,
) {

    val onTranslationClick: (Long) -> Unit = { translationId ->
        navigator.navigate(TranslationEditScreenDestination(translationID = translationId))
    }

    LazyColumn(contentPadding = paddingValues) {
        translations?.forEach { translation ->
            item {
                TranslationCard(translation = translation, onTranslationClick = {
                    onTranslationClick(
                        translation.id!!
                    )
                })
            }
        }
    }
}

@Composable
fun TranslationCard(translation: TranslationEntity, onTranslationClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onTranslationClick)
            .testTag("TestTagTranslationCard${translation.id}"),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Rounded Image
            val imagePainter = if (translation.image.isEmpty()) {
                painterResource(id = R.drawable.translation_placeholder)
            } else {
                rememberImagePainter(data = translation.image)
            }

            Image(
                painter = imagePainter,
                contentDescription = stringResource(id = R.string.translation_image),
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)), // Rounded corners
                contentScale = ContentScale.Crop
            )


            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // Original text
                Text(
                    text = translation.originalString,
                    maxLines = 1, // Limit to one line
                    overflow = TextOverflow.Ellipsis, // Show ellipsis if text is too long
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center horizontally
                )

                Spacer(modifier = Modifier.height(smallestMargin()))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center, // Center content horizontally
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = translation.originalLanguage,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(basicMargin())) // Add spacing before the arrow

                    Image(
                        painter = painterResource(id = R.drawable.translation_arrow),
                        contentDescription = stringResource(id = R.string.to),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(basicMargin())) // Add spacing before the arrow

                    Text(
                        text = translation.translatedLanguage,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.height(smallestMargin()))


                // Translated text
                Text(
                    text = translation.translatedString,
                    maxLines = 1, // Limit to one line
                    overflow = TextOverflow.Ellipsis, // Show ellipsis if text is too long
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center horizontally
                )
            }
        }
    }
}