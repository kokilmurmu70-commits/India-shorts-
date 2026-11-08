package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ControlsCatalog
import com.example.model.AppControlItem
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun IndiaShortsNavigationRail(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isDark by viewModel.isDarkTheme.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surface,
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(SaffronPrimary, Color.White, EmeraldSecondary)
                            )
                        )
                        .clickable { viewModel.navigateTo(AppScreen.SHORTS) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Logo",
                        tint = NavyDeep,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "INDIA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = SaffronPrimary
                )
                Text(
                    text = "SHORTS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationRailItem(
                    selected = currentScreen == AppScreen.SHORTS,
                    onClick = { viewModel.navigateTo(AppScreen.SHORTS) },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Shorts") },
                    label = { Text("Shorts", fontSize = 10.sp) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("rail_item_shorts")
                )

                NavigationRailItem(
                    selected = currentScreen == AppScreen.HOME || currentScreen == AppScreen.VIDEOS,
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 10.sp) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("rail_item_home")
                )

                NavigationRailItem(
                    selected = currentScreen == AppScreen.UPLOAD,
                    onClick = { viewModel.navigateTo(AppScreen.UPLOAD) },
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Upload") },
                    label = { Text("Upload", fontSize = 10.sp) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = EmeraldSecondary,
                        selectedTextColor = EmeraldSecondary,
                        indicatorColor = EmeraldSecondary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("rail_item_upload")
                )

                NavigationRailItem(
                    selected = currentScreen == AppScreen.TRENDING || currentScreen == AppScreen.MUSIC,
                    onClick = { viewModel.navigateTo(AppScreen.TRENDING) },
                    icon = { Icon(Icons.Default.Whatshot, contentDescription = "Trending") },
                    label = { Text("Trending", fontSize = 10.sp) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("rail_item_trending")
                )

                NavigationRailItem(
                    selected = currentScreen == AppScreen.CREATOR_STUDIO,
                    onClick = { viewModel.navigateTo(AppScreen.CREATOR_STUDIO) },
                    icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Creator") },
                    label = { Text("Creator", fontSize = 10.sp) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("rail_item_creator")
                )

                NavigationRailItem(
                    selected = currentScreen == AppScreen.OWNER_WALLET,
                    onClick = { viewModel.navigateTo(AppScreen.OWNER_WALLET) },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "PNB Wallet") },
                    label = { Text("Owner", fontSize = 10.sp) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = EmeraldSecondary,
                        selectedTextColor = EmeraldSecondary,
                        indicatorColor = EmeraldSecondary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("rail_item_owner")
                )

                NavigationRailItem(
                    selected = currentScreen == AppScreen.CONTROLS_DIRECTORY,
                    onClick = { viewModel.navigateTo(AppScreen.CONTROLS_DIRECTORY) },
                    icon = { Icon(Icons.Default.FormatListNumbered, contentDescription = "60 Controls") },
                    label = { Text("60 Items", fontSize = 10.sp) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("rail_item_controls")
                )

                NavigationRailItem(
                    selected = currentScreen == AppScreen.ADMIN_PANEL,
                    onClick = { viewModel.navigateTo(AppScreen.ADMIN_PANEL) },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                    label = { Text("Admin", fontSize = 10.sp) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("rail_item_admin")
                )
            }

            // Bottom utility actions: Theme & Language
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                IconButton(onClick = { viewModel.toggleTheme() }) {
                    Icon(
                        imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = if (isDark) AccentGold else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { showLanguageDialog = true }) {
                    Icon(
                        Icons.Default.Language,
                        contentDescription = "Language",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            viewModel = viewModel,
            onDismiss = { showLanguageDialog = false }
        )
    }
}

/**
 * Supporting Inspector Pane for Desktop and Laptop wide screens (>= 960dp).
 * Shows the active screen's exact 60 Labels and 60 Buttons placeholders,
 * owner status (Kokil Murmu / Punjab National Bank), and live revenue metrics.
 */
@Composable
fun DesktopPlatformInspectorPane(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val wallet by viewModel.ownerWallet.collectAsState()
    val allControls = ControlsCatalog.ALL_60_CONTROLS

    // Determine relevant domain for the active screen
    val activeDomain = when (currentScreen) {
        AppScreen.SHORTS -> "Shorts"
        AppScreen.HOME -> "Home"
        AppScreen.VIDEOS -> "Videos"
        AppScreen.UPLOAD -> "Upload"
        AppScreen.TRENDING -> "Trending"
        AppScreen.MUSIC -> "Music"
        AppScreen.CREATOR_STUDIO -> "Monetization"
        AppScreen.OWNER_WALLET -> "Owner Wallet"
        AppScreen.SPECIAL_REWARD -> "Admin Dashboard"
        AppScreen.SETTINGS -> "Settings"
        AppScreen.NOTIFICATIONS -> "Notifications"
        AppScreen.COPYRIGHT -> "Copyright"
        AppScreen.REPORTS -> "Reports"
        AppScreen.ADMIN_PANEL -> "Admin Dashboard"
        AppScreen.CONTROLS_DIRECTORY -> "All"
        AppScreen.OWNER_KYC -> "Owner Wallet"
    }

    val domainControls = remember(activeDomain) {
        if (activeDomain == "All") allControls
        else allControls.filter { it.domain.equals(activeDomain, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .border(1.dp, NavyBorder)
            .padding(14.dp)
    ) {
        // Desktop Header Card: Owner & Bank
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NavyCard,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DESKTOP WORKSTATION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    Surface(
                        color = AccentGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "DEMO DATA",
                            color = AccentGold,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Owner: ${wallet.ownerName}",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "Bank: ${wallet.bankName} (Backend Secured)",
                    fontSize = 11.sp,
                    color = SaffronLight
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Owner Share (86.67%): $${"%,.2f".format(wallet.ownerTotalEarningsUsd)} (DEMO)", fontSize = 10.sp, color = TextSecondary)
                    Text("Available: $${"%,.2f".format(wallet.availableBalanceUsd)} (DEMO)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Domain Controls Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Active Domain: $activeDomain",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Placeholders for 60 Labels & 60 Buttons",
                    fontSize = 10.sp,
                    color = SaffronPrimary
                )
            }
            TextButton(
                onClick = { viewModel.navigateTo(AppScreen.CONTROLS_DIRECTORY) }
            ) {
                Text("View All 60", fontSize = 11.sp, color = SaffronPrimary)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Live Domain Controls List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(domainControls, key = { it.id }) { item ->
                DesktopControlCard(
                    item = item,
                    onExecute = { viewModel.executeButtonAction(item.id) }
                )
            }
        }
    }
}

@Composable
fun DesktopControlCard(
    item: AppControlItem,
    onExecute: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("desktop_control_${item.id}"),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Item #${item.id}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary
                )
                Surface(
                    color = EmeraldSecondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = item.domain,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSecondary,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Label Placeholder
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Label, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.labelName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.description,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Button Placeholder & Trigger
            Button(
                onClick = onExecute,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .border(1.dp, NavyBorder, RoundedCornerShape(6.dp))
                    .testTag("desktop_btn_${item.id}"),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.buttonName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
        }
    }
}
