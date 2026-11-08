package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DummyData
import com.example.model.VideoCategory
import com.example.model.VideoItem
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun HomeScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val shorts by viewModel.shortsFeed.collectAsState()
    val longVideos by viewModel.longVideosFeed.collectAsState()
    val videoQuality by viewModel.videoQuality.collectAsState()
    val showSubtitles by viewModel.showSubtitles.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All") + VideoCategory.values().map { it.displayName }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Featured Creator Spotlight Banner (Label 2, Button 2)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("featured_creator_spotlight_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NavyCard
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(SaffronDark.copy(alpha = 0.5f), NavySurface)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = SaffronPrimary,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "PLATFORM FOUNDER SPOTLIGHT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "Label 2: Spotlight",
                                fontSize = 10.sp,
                                color = SaffronLight
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(SaffronPrimary, EmeraldSecondary))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("KM", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Kokil Murmu",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        Icons.Filled.Verified,
                                        contentDescription = "Verified Owner",
                                        tint = SaffronPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "Founder of INDIA SHORTS • 485K Subscribers",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "“INDIA SHORTS is built to celebrate regional roots, empowering creators across every Indian language with genuine monetization.”",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.executeButtonAction(2) },
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .testTag("btn_2")
                            ) {
                                Text("Button 2: Explore Channels", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.executeButtonAction(1) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .testTag("btn_1")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Button 1: Refresh Feed", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Horizontal Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaffronPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Trending Shorts Rail Preview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Whatshot, contentDescription = "Shorts", tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Trending Shorts",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                TextButton(onClick = { viewModel.navigateTo(AppScreen.SHORTS) }) {
                    Text("View All", fontSize = 12.sp, color = SaffronPrimary)
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(shorts) { shortItem ->
                    ShortPreviewCard(
                        video = shortItem,
                        onClick = {
                            val idx = shorts.indexOf(shortItem)
                            if (idx != -1) {
                                viewModel.executeButtonAction(4)
                            }
                            viewModel.navigateTo(AppScreen.SHORTS)
                        }
                    )
                }
            }
        }

        // 2. Home Page Banner Ad (Requirement 1.2: Home Page पर Banner Ads, AdMob Test Ad)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_banner_ad_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AccentGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "AD / SPONSORED (Google AdMob Test)",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "AdUnit: ca-app-pub-3940256099942544/6300978111",
                            fontSize = 8.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SaffronPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.FlightTakeoff, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "MakeMyTrip India Vacation Sale",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Get up to 40% OFF on flights & hotels across India with code INDIASHORTS.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { viewModel.recordSimulatedAdImpression("BANNER_HOME") },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Book Now", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Long Videos Header & Control Bar (Label 6, 7, 8 & Buttons 6, 7, 8)
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Long-Form Videos & Documentaries",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Label 6: Quality ($videoQuality) | Label 7: Subtitles (${if (showSubtitles) "ON" else "OFF"})",
                                fontSize = 11.sp,
                                color = SaffronPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { viewModel.executeButtonAction(6) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_6")
                        ) {
                            Text("Btn 6: Quality", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(7) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_7")
                        ) {
                            Text("Btn 7: Subtitles", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(8) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_8")
                        ) {
                            Text("Btn 8: Cinema", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        // Long Videos List
        items(longVideos) { video ->
            LongVideoCard(
                video = video,
                onPlay = { viewModel.navigateTo(AppScreen.SHORTS) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ShortPreviewCard(
    video: VideoItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(135.dp)
            .height(210.dp)
            .clickable(onClick = onClick)
            .testTag("short_card_${video.id}"),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(video.gradientStartHex),
                            NavyDeep,
                            Color(video.gradientEndHex)
                        )
                    )
                )
                .padding(10.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = video.durationText,
                    fontSize = 9.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }

            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = video.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${video.viewsCount / 1000}K views",
                    fontSize = 10.sp,
                    color = AccentGold
                )
            }
        }
    }
}

@Composable
fun LongVideoCard(
    video: VideoItem,
    onPlay: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPlay)
            .testTag("long_video_card_${video.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Thumbnail container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(video.gradientStartHex), NavyDeep, Color(video.gradientEndHex))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(32.dp))
                }

                Surface(
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = video.durationText,
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Info row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(video.creatorAvatarColor)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(video.creatorName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${video.creatorName} • ${video.viewsCount / 1000}K views • ${video.language}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
