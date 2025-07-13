package cz.mendelu.pef.xdostal8.saywhatsnap.model.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Language(
    var id: Long?,
    var code: String?,
)
