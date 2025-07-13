package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen

import android.net.Uri
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity

class TranslationEditData {
    var selectedImageURI: Uri = Uri.EMPTY
    var translation: TranslationEntity = TranslationEntity(
        category = "",
        date = 0,
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