package cz.mendelu.pef.xdostal8.saywhatsnap.architecture

data class CommunicationError(
    val code: Int,
    var message: String?,
    var secondaryMessage: String? = null)