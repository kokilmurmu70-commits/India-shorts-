package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.IndiaShortsTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: IndiaShortsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDark by viewModel.isDarkTheme.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val toastMessage by viewModel.toastMessage.collectAsState()

            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(toastMessage) {
                toastMessage?.let {
                    snackbarHostState.showSnackbar(
                        message = it.message,
                        duration = SnackbarDuration.Short
                    )
                    viewModel.clearToast()
                }
            }

            // BackHandler for sub-screens: if not on SHORTS, return to SHORTS
            if (currentScreen != AppScreen.SHORTS) {
                BackHandler {
                    viewModel.navigateTo(AppScreen.SHORTS)
                }
            }

            IndiaShortsTheme(darkTheme = isDark) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("root_responsive_container")
                ) {
                    val isCompact = maxWidth < 600.dp
                    val isMedium = maxWidth in 600.dp..959.dp
                    val isExpanded = maxWidth >= 960.dp

                    if (isCompact) {
                        // MOBILE COMPACT LAYOUT (< 600dp)
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("main_scaffold_mobile"),
                            topBar = {
                                IndiaShortsTopBar(viewModel = viewModel)
                            },
                            bottomBar = {
                                IndiaShortsBottomBar(viewModel = viewModel)
                            },
                            snackbarHost = {
                                SnackbarHost(hostState = snackbarHostState)
                            }
                        ) { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                ActiveScreenContent(currentScreen = currentScreen, viewModel = viewModel)
                            }
                        }
                    } else if (isMedium) {
                        // TABLET MEDIUM LAYOUT (600dp - 959dp)
                        Row(modifier = Modifier.fillMaxSize()) {
                            IndiaShortsNavigationRail(
                                viewModel = viewModel,
                                modifier = Modifier.testTag("nav_rail_tablet")
                            )
                            Scaffold(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .testTag("main_scaffold_tablet"),
                                topBar = {
                                    IndiaShortsTopBar(viewModel = viewModel)
                                },
                                snackbarHost = {
                                    SnackbarHost(hostState = snackbarHostState)
                                }
                            ) { innerPadding ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .background(MaterialTheme.colorScheme.background),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .widthIn(max = 760.dp)
                                            .fillMaxWidth()
                                    ) {
                                        ActiveScreenContent(currentScreen = currentScreen, viewModel = viewModel)
                                    }
                                }
                            }
                        }
                    } else {
                        // DESKTOP / LAPTOP EXPANDED DUAL-PANE LAYOUT (>= 960dp)
                        Row(modifier = Modifier.fillMaxSize()) {
                            IndiaShortsNavigationRail(
                                viewModel = viewModel,
                                modifier = Modifier.testTag("nav_rail_desktop")
                            )
                            Scaffold(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .testTag("main_scaffold_desktop"),
                                topBar = {
                                    IndiaShortsTopBar(viewModel = viewModel)
                                },
                                snackbarHost = {
                                    SnackbarHost(hostState = snackbarHostState)
                                }
                            ) { innerPadding ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .background(MaterialTheme.colorScheme.background)
                                ) {
                                    // Primary Screen Pane (Left, 60% of workspace)
                                    Box(
                                        modifier = Modifier
                                            .weight(1.35f)
                                            .fillMaxHeight()
                                            .testTag("desktop_primary_pane")
                                    ) {
                                        ActiveScreenContent(currentScreen = currentScreen, viewModel = viewModel)
                                    }

                                    // Secondary Supporting Pane (Right, 40% of workspace)
                                    // Displays active domain controls, 60 labels & buttons inspector, owner financials
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .background(MaterialTheme.colorScheme.surface)
                                            .testTag("desktop_supporting_pane")
                                    ) {
                                        DesktopPlatformInspectorPane(viewModel = viewModel)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveScreenContent(
    currentScreen: AppScreen,
    viewModel: IndiaShortsViewModel
) {
    when (currentScreen) {
        AppScreen.SHORTS -> ShortsScreen(viewModel = viewModel)
        AppScreen.HOME, AppScreen.VIDEOS -> HomeScreen(viewModel = viewModel)
        AppScreen.UPLOAD -> UploadScreen(viewModel = viewModel)
        AppScreen.TRENDING, AppScreen.MUSIC -> TrendingMusicScreen(viewModel = viewModel)
        AppScreen.CREATOR_STUDIO -> CreatorMonetizationScreen(viewModel = viewModel)
        AppScreen.OWNER_WALLET -> OwnerWalletScreen(viewModel = viewModel)
        AppScreen.SPECIAL_REWARD -> SpecialRewardScreen(viewModel = viewModel)
        AppScreen.SETTINGS, AppScreen.NOTIFICATIONS -> ProfileAndAuthScreen(viewModel = viewModel)
        AppScreen.COPYRIGHT, AppScreen.REPORTS -> CopyrightAndReportsScreen(viewModel = viewModel)
        AppScreen.ADMIN_PANEL -> AdminDashboardScreen(viewModel = viewModel)
        AppScreen.CONTROLS_DIRECTORY -> ControlCatalogScreen(viewModel = viewModel)
        AppScreen.OWNER_KYC -> OwnerKycScreen(viewModel = viewModel)
    }
}
