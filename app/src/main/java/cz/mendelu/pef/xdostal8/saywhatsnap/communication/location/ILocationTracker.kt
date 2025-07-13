package cz.mendelu.pef.xdostal8.saywhatsnap.communication.location

import android.location.Location


interface ILocationTracker {
    suspend fun getCurrentLocation(): Location?
}
