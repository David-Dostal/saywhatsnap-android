package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen

interface PinEditActions {

    fun onNameChange(text: String)

    fun onCategoryChange(text: String)

    fun onDescriptionChange(text: String)

    fun saveTranslation()
}