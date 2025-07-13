package cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.viewmodels

import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppIntroViewModel @Inject constructor(
    private val dataStoreRepository: IDataStoreRepository
) : BaseViewModel() {

    suspend fun setFirstRun() {
        dataStoreRepository.setFirstRun()
    }
}