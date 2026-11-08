package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val wallet by viewModel.ownerWallet.collectAsState()
    val reward by viewModel.specialReward.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Command Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INDIA SHORTS Master Admin",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Surface(
                            color = SaffronPrimary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "KOKIL MURMU",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Global Platform Oversight • Direct PNB Settlement • Policy & Content Moderation",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Platform Health Analytics Grid (Label 58)
        item {
            Text("Label 58: Master Global Platform Telemetry", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminStatCard(title = "Total Active Users", value = "1,842,500", modifier = Modifier.weight(1f))
                    AdminStatCard(title = "Verified Creators", value = "42,100", modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminStatCard(title = "Verified Ads Revenue", value = "₹${"%,.0f".format(wallet.totalVerifiedRevenueInr)}", modifier = Modifier.weight(1f), isAccent = true)
                    AdminStatCard(title = "Owner Share (60%)", value = "₹${"%,.0f".format(wallet.ownerShareInr)}", modifier = Modifier.weight(1f), isAccent = true)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminStatCard(title = "Creator Pool (40%)", value = "₹${"%,.0f".format(wallet.totalCreatorPoolInr)}", modifier = Modifier.weight(1f))
                    AdminStatCard(title = "Total Eligible Views", value = "%,d".format(wallet.totalPlatformEligibleViews), modifier = Modifier.weight(1f))
                }
            }
        }

        // REQUIREMENT 6: OWNER DASHBOARD - MEMBERSHIP OVERSIGHT & SETTINGS
        item {
            val oversight by viewModel.ownerMembershipOversight.collectAsState()
            var showCommissionSlider by remember { mutableStateOf(false) }
            var commissionInput by remember { mutableStateOf(oversight.ownerPlatformCommissionPercent.toInt().toString()) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("owner_membership_oversight_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CardMembership, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Platform Creator Membership Oversight",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            color = EmeraldSecondary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Owner: Kokil Murmu",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4-Card Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Total Members", fontSize = 10.sp, color = TextSecondary)
                                Text("${oversight.platformTotalMembers}", fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Active Creators", fontSize = 10.sp, color = TextSecondary)
                                Text("${oversight.totalActiveCreatorsWithMembership}", fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Gross Membership Rev", fontSize = 10.sp, color = TextSecondary)
                                Text("₹${"%,.0f".format(oversight.platformGrossMembershipRevenueInr)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = EmeraldSecondary)
                            }
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Owner Commission (20%)", fontSize = 10.sp, color = TextSecondary)
                                Text("₹${"%,.0f".format(oversight.ownerTotalMembershipRevenueInr)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = SaffronPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dispute & Refund Management (Rule 6)
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Refunds Processed: ₹${"%,.0f".format(oversight.totalRefundsProcessedInr)}", fontSize = 11.sp)
                                Text("Pending Disputes: ${oversight.pendingDisputesCount}", fontSize = 11.sp, color = AccentRed, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.processMemberRefund(199.0, "Accidental subscription renewal within 24h") },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(34.dp)
                                        .testTag("btn_process_member_refund")
                                ) {
                                    Text("Resolve Dispute / Refund ₹199", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                                }

                                OutlinedButton(
                                    onClick = { showCommissionSlider = !showCommissionSlider },
                                    modifier = Modifier
                                        .weight(0.9f)
                                        .height(34.dp)
                                        .testTag("btn_toggle_commission_settings")
                                ) {
                                    Text("Commission: ${oversight.ownerPlatformCommissionPercent.toInt()}%", fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    // Commission Adjuster
                    if (showCommissionSlider) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = commissionInput,
                                onValueChange = { commissionInput = it },
                                label = { Text("Owner Commission % (5-50)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    val percent = commissionInput.toDoubleOrNull() ?: 20.0
                                    viewModel.updateMembershipCommission(percent)
                                    showCommissionSlider = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary)
                            ) {
                                Text("Save %", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Admin Action Console (Buttons 58, 59, 60)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Administrative Operations & Payout Dispatch",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.executeButtonAction(58) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("btn_58")
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Btn 58: Review Flagged Content Queue", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(59) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("btn_59_admin")
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Btn 59: Verify ₹10 Lakh Creator Milestone Claim", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.simulateScheduled1stMonthlyPayoutCycle() },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("btn_60_admin")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Btn 60: Trigger Scheduled 1st Monthly Payout Cycle", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick Links to Secondary Platforms
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Platform Management Shortcuts", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.OWNER_KYC) },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("btn_admin_goto_kyc")
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Owner KYC & Verification Vault", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppScreen.CONTROLS_DIRECTORY) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("60 Controls", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppScreen.OWNER_WALLET) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Owner Wallet", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppScreen.COPYRIGHT) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Copyright ID", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    isAccent: Boolean = false
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAccent) EmeraldSecondary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
