@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.basicMargin
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.getBackGroundColor
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.getBarTitleColor
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.getCurrentPrimaryColor
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.titleLarge

const val TestContent = "TestContent"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyBaseScreen(
    customTitle: @Composable () -> Unit = {},
    topBarText: String? = null,
    onBackClick: (() -> Unit)? = null,
    navigator: DestinationsNavigator,
    currentDestinationPosition: Int? = null,
    showSidePadding: Boolean = false,
    drawFullScreenContent: Boolean = false,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    showLoading: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    // Define the colors - change them based on the stylizeTopBar flag
    val containerColor: Color = getCurrentPrimaryColor()
    val titleContentColor: Color = getBarTitleColor()


    Scaffold(
        containerColor = getBackGroundColor(),
        floatingActionButton = floatingActionButton,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        if (topBarText != null) {
                            Text(
                                text = topBarText,
                                style = titleLarge(),
                                color = titleContentColor,
                                modifier = Modifier
                                    .padding(start = 0.dp)
                                    .weight(1.5f)
                            )
                        } else {
                            customTitle()
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = containerColor, // Use dynamic color
                    titleContentColor = titleContentColor // Use dynamic color
                ),
                actions = actions,
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = titleContentColor
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (currentDestinationPosition != null) {

                MyBottomBar(currentDestinationPosition, navigator)
            }
        }

    ) {
        if (placeholderScreenContent != null) {
            PlaceHolderScreen(
                modifier = Modifier.padding(it),
                content = placeholderScreenContent
            )
        } else if (showLoading) {
            LoadingScreen(modifier = Modifier.padding(it))
        } else {
            if (!drawFullScreenContent) {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .testTag(TestContent),

                    ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .padding(if (!showSidePadding) basicMargin() else 0.dp)
                        ) {
                            content(it)
                        }
                    }
                }
            } else {
                content(it)
            }
        }
    }

}
