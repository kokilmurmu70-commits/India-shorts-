package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun SpecialRewardScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val reward by viewModel.specialReward.collectAsState()
    val progress = (reward.currentEligibleViews.toFloat() / reward.targetViews.toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Grand Trophy Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(AccentGold.copy(alpha = 0.8f), SaffronDark, NavyDeep)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = "Trophy", tint = Color.White, modifier = Modifier.size(40.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "SPECIAL CREATOR REWARD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "₹10,00,000 (10 Lakh Rupees)",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Target: 1.10 BILLION (1,100,000,000) Verified Views",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentGold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Established and guaranteed by Founder Kokil Murmu for exceptional original creators who achieve global reach on INDIA SHORTS.",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Live Progress Gauge (Label 59 & Button 59)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Label 59: Special Creator Reward Tracker",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${"%,d".format(reward.currentEligibleViews)} views",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${(progress * 100).toInt()}% Done",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = AccentGold,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Remaining to unlock ₹10,00,000: ${"%,d".format(reward.targetViews - reward.currentEligibleViews)} verified views",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.verifySpecialRewardMilestone() },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("btn_59")
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Btn 59: Verify ₹10 Lakh Creator Milestone Claim", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Rules & Eligibility Matrix
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Official Program Guidelines", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        "1. Content must be 100% original authorship without third-party copyright claims.",
                        "2. Views are verified using multi-layer anti-bot fingerprinting to filter duplicate/simulated hits.",
                        "3. Zero policy strikes on channel for hate speech, pornography, or minor safety violations.",
                        "4. Payout is processed directly to verified Indian bank accounts via Direct NACH/RTGS."
                    ).forEach { rule ->
                        Text(rule, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
