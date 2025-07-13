package cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.xdostal8.saywhatsnap.architecture.BaseActivity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.viewmodels.MainActivityViewModel
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.SayWhatSnapTheme
import cz.mendelu.pef.xdostal8.saywhatsnap.utils.LocaleUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityViewModel>(MainActivityViewModel::class.java) {
    override val viewModel by viewModels<MainActivityViewModel>()

    companion object {
        fun createIntent(context: AppCompatActivity): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                LocaleUtils.setLocale(context, viewModel.dataStoreRepository)
            }

            SayWhatSnapTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
