package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SongItem
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun TrendingMusicScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val songs by viewModel.songs.collectAsState()
    val trendingCountry by viewModel.trendingCountry.collectAsState()
    val trendingState by viewModel.trendingState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0: Trending, 1: Authorized Music Library

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search & Filter Header (Label 12, 13 & Button 12, 13)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Label 12: Unified Global Video, Creator & Sound Radar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search Santali, Hindi, comedy, music, #vlogs...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.clearSearchHistory() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trending_search_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.executeButtonAction(12) },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_12")
                        ) {
                            Text("Btn 12: Search Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.clearSearchHistory() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_13")
                        ) {
                            Text("Btn 13: Clear Cache", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Trending Geolocation Filters (Label 14, 15, 16 & Button 14, 15, 16)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Trending Geolocation Radar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Country: $trendingCountry • State: $trendingState",
                                fontSize = 11.sp,
                                color = SaffronPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { viewModel.executeButtonAction(14) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_14")
                        ) {
                            Text("Btn 14: Country", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(15) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_15")
                        ) {
                            Text("Btn 15: State", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.refreshTrendingAlgorithm() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_16")
                        ) {
                            Text("Btn 16: Algorithm", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        // Sub-tabs: Trending Charts vs Authorized Music
        item {
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = SaffronPrimary
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = { Text("Trending Hashtags & Audio", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = { Text("Authorized Audio Library", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (activeTab == 0) {
            // Trending Hashtags Grid
            item {
                Text("Hot Viral Hashtags in $trendingCountry", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                val tags = listOf("#IndiaShorts", "#SantaliCulture", "#DesiComedy", "#VillageVlog", "#JharkhandTourism", "#TechIndia", "#TribalArt")
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    tags.forEachIndexed { i, tag ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setSearchQuery(tag)
                                    viewModel.executeButtonAction(12)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${i + 1}.", fontWeight = FontWeight.Bold, color = SaffronPrimary, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(tag, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                }
                                Text("${(150 - i * 18)}K Shorts", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }

        // Authorized Audio Library (Label 17, 18 & Button 17, 18)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Authorized Music Library",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Label 17: Certified 100% Royalty-Free & Copyright Cleared",
                        fontSize = 11.sp,
                        color = EmeraldSecondary
                    )
                }
            }
        }

        items(songs) { song ->
            SongItemCard(
                song = song,
                onPreview = { viewModel.executeButtonAction(17) },
                onUse = { viewModel.executeButtonAction(18) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SongItemCard(
    song: SongItem,
    onPreview: () -> Unit,
    onUse: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("song_card_${song.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavySurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Audiotrack, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(song.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                        Text("${song.artist} • ${song.language} (${song.duration})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(song.usageCount, fontSize = 10.sp, color = AccentGold, fontWeight = FontWeight.SemiBold)
                    }
                }

                Surface(
                    color = EmeraldSecondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "✓ LICENSED",
                        color = EmeraldSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPreview,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .testTag("btn_17")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Btn 17: Preview", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                }

                Button(
                    onClick = onUse,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .testTag("btn_18")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Btn 18: Use in Short", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
