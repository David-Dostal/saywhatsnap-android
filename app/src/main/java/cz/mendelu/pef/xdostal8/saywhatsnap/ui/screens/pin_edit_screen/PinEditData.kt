package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen

import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity

class PinEditData {
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
        visible = true
    )
}