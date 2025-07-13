package cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.viewmodels

import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val dataStoreRepository: IDataStoreRepository
) : BaseViewModel()