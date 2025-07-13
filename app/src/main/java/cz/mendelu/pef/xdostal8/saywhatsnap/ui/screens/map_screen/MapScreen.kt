package cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.map_screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.GridBasedAlgorithm
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.map.CustomMapRenderer
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyBaseScreen
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MyBottomSheet
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.MySearchableDropdownMenu
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.PermissionsRequestDialog
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.SettingsScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.getCurrentPrimaryColor


@Destination
@Composable
fun MapScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = hiltViewModel<MapViewModel>()

    val mapScreenUIState by remember { mutableStateOf(viewModel.mapScreenUIState) }

    val categoriesUIState by remember { mutableStateOf(viewModel.categoriesUIState) }

    var uiData: MapScreenData by remember { mutableStateOf(viewModel.uiData) }

    viewModel.screenUIState.value.let {
        when (it) {
            MapUIState.Default -> {

            }

            MapUIState.Loading -> {
                LaunchedEffect(it) {
                    viewModel.getUserCoords()
                    viewModel.getAllCategories()
                    viewModel.loadAllPlaces()
                }
            }

            MapUIState.FilterChanged -> {
                uiData = viewModel.uiData
                viewModel.screenUIState.value = MapUIState.Default
            }
        }

    }


    MyBaseScreen(topBarText = stringResource(R.string.title_map_screen),
        showLoading = mapScreenUIState.value.loading || categoriesUIState.value.loading,
        floatingActionButton =
        {
            val categories = categoriesUIState.value.data
            if (categories != null) {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(16.dp)
                        .background(
                            getCurrentPrimaryColor(),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    MySearchableDropdownMenu(
                        options = categories,
                        currentValue = viewModel.uiData.selectedCategory,
                        onValueChange = { viewModel.changeSelectedCategory(it) },
                        label = R.string.category,
                        error = "",
                        toastText = stringResource(R.string.category_filter_selected)
                    )
                }
            }
        },
        drawFullScreenContent = true,
        placeholderScreenContent =
        if (viewModel.getPlaceholderContentText() != null) {
            PlaceholderScreenContent(
                null,
                text = stringResource(id = viewModel.getPlaceholderContentText()!!)
            )
        } else null,
        currentDestinationPosition = 2,
        navigator = navigator,
        actions = {
            FilledIconButton(onClick = {
                navigator.navigate(SettingsScreenDestination)

            }) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.settings_description)
                )
            }
        })
    {
            MapScreenContent(
                locations = mapScreenUIState.value.data,
                navigator = navigator,
                actions = viewModel,
                latitude = uiData.latitude,
                longitude = uiData.longitude
            )
        }
}

@SuppressLint("PotentialBehaviorOverride")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreenContent(
    locations: List<TranslationEntity>?,
    navigator: DestinationsNavigator,
    actions: MapActions,
    latitude: Double,
    longitude: Double
) {


    var selectedTranslation by rememberSaveable { mutableStateOf<TranslationEntity?>(null) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }


    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false
            )
        )
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 5f)
    }
    PermissionsRequestDialog(
            icon = Icons.Filled.LocationOn,
            title = stringResource(R.string.permissions_location),
            message = stringResource(R.string.permissions_location_message),
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            onPermissionsGranted = {
                actions.getUserCoords()
                cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 5f)
            }
        )

        val context = LocalContext.current
        var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
        var clusterManager by remember { mutableStateOf<ClusterManager<TranslationEntity>?>(null) }
        var clusterRenderer by remember { mutableStateOf<ClusterRenderer<TranslationEntity>?>(null) }

        GoogleMap(
            modifier = Modifier.fillMaxHeight().testTag("map"),
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
        ) {

            MapEffect(locations) { map ->
                if (googleMap == null) googleMap = map
                if (clusterManager == null) {
                    clusterManager = ClusterManager(context, googleMap)
                    clusterRenderer = CustomMapRenderer(context, googleMap!!, clusterManager!!)
                    clusterManager?.apply {
                        algorithm = GridBasedAlgorithm()
                        renderer = clusterRenderer
                    }
                }

                clusterManager?.setOnClusterItemClickListener { item ->
                    // Here 'item' is the clicked TranslationEntity
                    selectedTranslation = item  // Save the clicked TranslationEntity
                    showBottomSheet = true
                    true  // Return true to indicate that we've handled the click
                }

                clusterManager?.clearItems()  // Clear existing items before adding new ones
                clusterManager?.addItems(
                    locations ?: listOf()
                )  // Use the 'locations' list directly
                clusterManager?.cluster()
            }


            googleMap?.setOnCameraIdleListener {
                clusterManager?.cluster()
            }
        }


        if (showBottomSheet && selectedTranslation != null) {
            MyBottomSheet(
                isVisible = showBottomSheet,
                translation = selectedTranslation!!,
                onDismiss = { showBottomSheet = false },
                navigator = navigator
            )
        }

}