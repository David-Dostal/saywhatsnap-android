package cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.states.SplashScreenUiState
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.viewmodels.SplashScreenActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val viewModel: SplashScreenActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }

        lifecycleScope.launchWhenResumed {
            viewModel.splashScreenState.collect { value ->
                when (value) {
                    is SplashScreenUiState.Default -> {
                        viewModel.checkAppState()
                    }

                    SplashScreenUiState.ContinueToApp -> {
                        continueToAList()
                    }

                    is SplashScreenUiState.RunForAFirstTime -> {
                        runAppIntro()
                    }
                }
            }
        }

    }

    private fun runAppIntro() {
        startActivity(AppIntroActivity.createIntent(this))
        finish()
    }

    private fun continueToAList() {
        startActivity(MainActivity.createIntent(this))
        finish()
    }
}

