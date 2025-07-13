package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen

import android.net.Uri
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import java.util.Calendar

class TranslateData {
    var selectedImageURI: Uri = Uri.EMPTY
    var selectedSaveOption: Int = 0
    var translation: TranslationEntity = TranslationEntity(
        category = "",
        date = Calendar.getInstance().timeInMillis,
        description = "",
        name = "",
        image = "",
        latitude = 0.0,
        longitude = 0.0,
        originalLanguage = "",
        originalString = "",
        translatedLanguage = "",
        translatedString = "",
        visible = false
    )
}