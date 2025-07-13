package cz.mendelu.pef.xdostal8.saywhatsnap.model

import java.io.Serializable

open class UiState<T, E>
    (
    var loading: Boolean = true,
    var data: T? = null,
    var errors: E? = null) : Serializable