package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.model.SupportedLanguage
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndiaShortsTopBar(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isDark by viewModel.isDarkTheme.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Logo & Brand with Tricolor gradient
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.SHORTS) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(SaffronPrimary, Color.White, EmeraldSecondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Logo",
                            tint = NavyDeep,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "INDIA SHORTS",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                letterSpacing = 0.5.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = EmeraldSecondary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "GLOBAL",
                                    color = EmeraldSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Created by Kokil Murmu",
                            fontSize = 11.sp,
                            color = SaffronPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Top Actions: Language, Theme, 60-Controls Directory, Owner Wallet
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Language Chip
                    FilledTonalButton(
                        onClick = { showLanguageDialog = true },
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("top_bar_language_btn"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = "Language",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = selectedLanguage.nativeName, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Theme Toggle
                    IconButton(
                        onClick = { viewModel.toggleTheme() },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("top_bar_theme_toggle")
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = if (isDark) AccentGold else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 60-Controls Directory Inspector Button
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.CONTROLS_DIRECTORY) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentScreen == AppScreen.CONTROLS_DIRECTORY) SaffronPrimary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .testTag("top_bar_60_controls_btn")
                    ) {
                        Icon(
                            Icons.Default.FormatListNumbered,
                            contentDescription = "60 Controls Inspector",
                            tint = if (currentScreen == AppScreen.CONTROLS_DIRECTORY) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Quick Category / Navigation Ribbon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuickNavPill(
                    label = "Shorts",
                    selected = currentScreen == AppScreen.SHORTS,
                    onClick = { viewModel.navigateTo(AppScreen.SHORTS) }
                )
                QuickNavPill(
                    label = "Home",
                    selected = currentScreen == AppScreen.HOME,
                    onClick = { viewModel.navigateTo(AppScreen.HOME) }
                )
                QuickNavPill(
                    label = "Trending",
                    selected = currentScreen == AppScreen.TRENDING,
                    onClick = { viewModel.navigateTo(AppScreen.TRENDING) }
                )
                QuickNavPill(
                    label = "Creator Hub",
                    selected = currentScreen == AppScreen.CREATOR_STUDIO,
                    onClick = { viewModel.navigateTo(AppScreen.CREATOR_STUDIO) }
                )
                QuickNavPill(
                    label = "Owner PNB",
                    selected = currentScreen == AppScreen.OWNER_WALLET,
                    onClick = { viewModel.navigateTo(AppScreen.OWNER_WALLET) },
                    isAccent = true
                )
                QuickNavPill(
                    label = "Admin",
                    selected = currentScreen == AppScreen.ADMIN_PANEL,
                    onClick = { viewModel.navigateTo(AppScreen.ADMIN_PANEL) }
                )
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

@Composable
fun QuickNavPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    isAccent: Boolean = false
) {
    val bgColor = when {
        selected && isAccent -> EmeraldSecondary
        selected -> SaffronPrimary
        isAccent -> EmeraldSecondary.copy(alpha = 0.15f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when {
        selected -> Color.White
        isAccent -> EmeraldSecondary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp)),
        color = bgColor
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun IndiaShortsBottomBar(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.SHORTS,
            onClick = { viewModel.navigateTo(AppScreen.SHORTS) },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Shorts") },
            label = { Text("Shorts", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SaffronPrimary,
                selectedTextColor = SaffronPrimary,
                indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
            ),
            modifier = Modifier.testTag("nav_item_shorts")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME || currentScreen == AppScreen.VIDEOS,
            onClick = { viewModel.navigateTo(AppScreen.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SaffronPrimary,
                selectedTextColor = SaffronPrimary,
                indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
            ),
            modifier = Modifier.testTag("nav_item_home")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.UPLOAD,
            onClick = { viewModel.navigateTo(AppScreen.UPLOAD) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(SaffronPrimary, EmeraldSecondary))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Upload", tint = Color.White)
                }
            },
            label = { Text("Upload", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier.testTag("nav_item_upload")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.TRENDING || currentScreen == AppScreen.MUSIC,
            onClick = { viewModel.navigateTo(AppScreen.TRENDING) },
            icon = { Icon(Icons.Default.Whatshot, contentDescription = "Trending") },
            label = { Text("Trending", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SaffronPrimary,
                selectedTextColor = SaffronPrimary,
                indicatorColor = SaffronPrimary.copy(alpha = 0.2f)
            ),
            modifier = Modifier.testTag("nav_item_trending")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.OWNER_WALLET || currentScreen == AppScreen.CREATOR_STUDIO,
            onClick = { viewModel.navigateTo(AppScreen.OWNER_WALLET) },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet") },
            label = { Text("Wallet", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldSecondary,
                selectedTextColor = EmeraldSecondary,
                indicatorColor = EmeraldSecondary.copy(alpha = 0.2f)
            ),
            modifier = Modifier.testTag("nav_item_wallet")
        )
    }
}

@Composable
fun LanguageSelectionDialog(
    viewModel: IndiaShortsViewModel,
    onDismiss: () -> Unit
) {
    val languages = com.example.data.DummyData.SUPPORTED_LANGUAGES
    val selected by viewModel.selectedLanguage.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Select Platform Language", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Supports 20 Indian & International Languages", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        text = {
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(languages.size) { index ->
                    val lang = languages[index]
                    val isCurrent = lang.code == selected.code
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isCurrent) SaffronPrimary.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable {
                                viewModel.setLanguage(lang)
                                onDismiss()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${lang.nativeName} (${lang.englishName})",
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCurrent) SaffronPrimary else MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                            Text(
                                text = lang.region,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isCurrent) {
                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = SaffronPrimary)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}
