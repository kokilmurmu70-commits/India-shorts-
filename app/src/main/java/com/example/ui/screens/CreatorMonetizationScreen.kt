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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MonetizationStatus
import com.example.ui.theme.*
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun CreatorMonetizationScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val m by viewModel.monetization.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    var testViewsInput by remember { mutableStateOf("10000") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Creator Studio Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(user.avatarColorHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(user.name.take(1), fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = SaffronPrimary, modifier = Modifier.size(16.dp))
                            }
                            Text(user.username, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${user.followersCount} Followers • ${user.bio.take(40)}...", fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    Surface(
                        color = when (m.status) {
                            MonetizationStatus.MONETIZED -> EmeraldSecondary.copy(alpha = 0.2f)
                            MonetizationStatus.ELIGIBLE -> SaffronPrimary.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = m.status.label,
                            color = when (m.status) {
                                MonetizationStatus.MONETIZED -> EmeraldSecondary
                                MonetizationStatus.ELIGIBLE -> SaffronPrimary
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // REQUIREMENT 2: CREATOR KYC EXEMPTION NOTICE
        item {
            Surface(
                color = EmeraldSecondary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("creator_kyc_exemption_banner")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Creator KYC: Not Required",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSecondary
                        )
                        Text(
                            text = "KYC verification is exclusively required from the Platform Owner. Creators can upload, monetize, and withdraw earnings without submitting KYC documents.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Monetization Eligibility Progress Tracker (Label 40, 41)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Label 40: Creator Partner Program Eligibility",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    Text(
                        text = "Official Targets: 500 Followers + 2,000 Public Watch Hours OR 100,000 Eligible Shorts Views",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Metric 1: Followers
                    MetricProgressBar(
                        title = "Followers Target",
                        current = m.followers.toLong(),
                        target = m.followerTarget.toLong(),
                        suffix = "followers"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Metric 2: Watch Hours
                    MetricProgressBar(
                        title = "Public Watch Hours Target",
                        current = m.watchHours.toLong(),
                        target = m.watchHoursTarget.toLong(),
                        suffix = "hrs"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Metric 3: Shorts Views
                    MetricProgressBar(
                        title = "Eligible Shorts Views Target",
                        current = m.shortsViews,
                        target = m.shortsViewsTarget,
                        suffix = "views"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.executeButtonAction(40) },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_40")
                        ) {
                            Text("Btn 40: Apply Partner", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(41) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_41")
                        ) {
                            Text("Btn 41: Check Rules", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        // Creator Earnings & Wallet (Label 43, 44 & Button 48 $200 Minimum Limit)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Label 43: Creator Wallet & Balances",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${"%,.2f".format(m.availableBalanceUsd)}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Available Balance • Pending Balance: $${"%,.2f".format(m.pendingBalanceUsd)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = if (m.availableBalanceUsd >= m.minimumWithdrawalUsd) EmeraldSecondary.copy(alpha = 0.15f) else AccentRed.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (m.availableBalanceUsd >= m.minimumWithdrawalUsd) Icons.Default.CheckCircle else Icons.Default.Info,
                                contentDescription = null,
                                tint = if (m.availableBalanceUsd >= m.minimumWithdrawalUsd) EmeraldSecondary else AccentRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Minimum Creator Withdrawal: $200.00\n${if (m.availableBalanceUsd >= m.minimumWithdrawalUsd) "Eligible to withdraw now." else "Balance under $200 threshold."}",
                                fontSize = 11.sp,
                                color = if (m.availableBalanceUsd >= m.minimumWithdrawalUsd) EmeraldSecondary else AccentRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.requestCreatorWithdrawal() },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("btn_48")
                        ) {
                            Text("Btn 48: Withdraw ($200 Min)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.executeButtonAction(44) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("btn_44")
                        ) {
                            Text("Btn 44: Statement", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Creator Comprehensive Revenue Dashboard (Requirement 5)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Creator Ads & Earnings Dashboard",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                        Surface(
                            color = EmeraldSecondary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "40% CREATOR POOL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Views row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Total Views", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("%,d".format(m.creatorTotalViews), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Eligible Views", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("%,d".format(m.creatorEligibleViews), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EmeraldSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Earnings Grid
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Estimated Earnings", fontSize = 10.sp, color = TextSecondary)
                                    Text("₹${"%,.2f".format(m.estimatedEarningsInr)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                                }
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Verified Earnings", fontSize = 10.sp, color = TextSecondary)
                                    Text("₹${"%,.2f".format(m.verifiedEarningsInr)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EmeraldSecondary)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Approved Earnings", fontSize = 10.sp, color = TextSecondary)
                                    Text("₹${"%,.2f".format(m.approvedEarningsInr)}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Withdrawable Balance", fontSize = 10.sp, color = TextSecondary)
                                    Text("₹${"%,.2f".format(m.withdrawableBalanceInr)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Monthly Earnings (Cycle Active): ₹${"%,.2f".format(m.monthlyEarningsInr)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // REQUIREMENT 5: CREATOR MEMBERSHIP DASHBOARD
        item {
            val membershipState by viewModel.creatorMembership.collectAsState()
            var editingPlanId by remember { mutableStateOf<String?>(null) }
            var priceEditInput by remember { mutableStateOf("") }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("creator_membership_dashboard_card"),
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
                            Icon(
                                Icons.Default.CardMembership,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Creator Membership Dashboard (JOIN)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            color = Color(0xFF0284C7).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "80% Creator Share",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Metrics Grid: Total Members, Active Members, Monthly Earnings
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
                                Text("${membershipState.totalMembers}", fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Active Members", fontSize = 10.sp, color = TextSecondary)
                                Text("${membershipState.activeMembers}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = EmeraldSecondary)
                            }
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Monthly Net (80%)", fontSize = 10.sp, color = TextSecondary)
                                Text("₹${"%,.0f".format(membershipState.creatorNetMembershipEarningsInr)}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = SaffronPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Creator Membership Plans Management (Rule 2)
                    Text("Your Membership Plans & Pricing (Monthly):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        membershipState.plans.forEach { plan ->
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(plan.level.badgeColorHex))
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(plan.level.displayName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "₹${"%.0f".format(plan.monthlyPriceInr)}/mo",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 13.sp,
                                                color = SaffronPrimary
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            IconButton(
                                                onClick = {
                                                    editingPlanId = plan.id
                                                    priceEditInput = "${plan.monthlyPriceInr.toInt()}"
                                                },
                                                modifier = Modifier.size(26.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "Edit Price", modifier = Modifier.size(14.dp), tint = TextSecondary)
                                            }
                                        }
                                    }

                                    // Plan Perks summary
                                    Text(
                                        text = "Perks: ${plan.perks.joinToString(" • ")}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }

                    // Price Edit Dialog for Creator Plan
                    if (editingPlanId != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = priceEditInput,
                                onValueChange = { priceEditInput = it },
                                label = { Text("Set New Monthly Price (₹)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    val newP = priceEditInput.toDoubleOrNull() ?: 59.0
                                    viewModel.updateMembershipPlanPrice(editingPlanId!!, newP)
                                    editingPlanId = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary)
                            ) {
                                Text("Save Price")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Membership Features / Exclusive Perks Info Card (Rule 4)
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Active Member Perks Provided:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                            Text("• Exclusive Members-Only Videos & Early Access", fontSize = 10.sp)
                            Text("• Special Member Badge in comments & livestreams", fontSize = 10.sp)
                            Text("• Exclusive Community Posts & Priority Replies", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Live Revenue Formula Calculator (40% Creator Pool / 60% Owner Share)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Label 42: Transparent Revenue Sharing Calculator (40/60 Split)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    Text(
                        text = "Formula: Creator Earning = (Total Creator Pool × Creator Eligible Views) ÷ All Creators' Eligible Views",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = testViewsInput,
                        onValueChange = { testViewsInput = it },
                        label = { Text("Enter Creator's Eligible Views (e.g. 10000)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val creatorViews = testViewsInput.toLongOrNull() ?: 10000L
                    val totalPlatformRevenue = 10000.0 // Example base ₹10,000
                    val allCreatorsViews = 100000L      // Example base 1,00,000
                    val creatorPool = totalPlatformRevenue * 0.40 // ₹4,000
                    val ownerShare = totalPlatformRevenue * 0.60  // ₹6,000
                    val calculatedCreatorEarnings = (creatorPool * creatorViews) / allCreatorsViews.toDouble()

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Ads Revenue (Verified Base):", fontSize = 11.sp)
                                Text("₹${"%,.2f".format(totalPlatformRevenue)}", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("40% Creator Pool (All Creators):", fontSize = 11.sp, color = EmeraldSecondary)
                                Text("₹${"%,.2f".format(creatorPool)}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = EmeraldSecondary)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("60% Owner Share (Kokil Murmu):", fontSize = 11.sp, color = SaffronPrimary)
                                Text("₹${"%,.2f".format(ownerShare)}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SaffronPrimary)
                            }
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Calculated Earning for $creatorViews views:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("₹${"%,.2f".format(calculatedCreatorEarnings)}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.calculateSampleRevenue(totalPlatformRevenue, allCreatorsViews, creatorViews) },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("btn_43")
                    ) {
                        Text("Btn 43: Apply 40/60 Revenue Breakdown", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
fun MetricProgressBar(
    title: String,
    current: Long,
    target: Long,
    suffix: String
) {
    val progress = (current.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    val isComplete = current >= target

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "${"%,d".format(current)} / ${"%,d".format(target)} $suffix",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isComplete) EmeraldSecondary else SaffronPrimary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = if (isComplete) EmeraldSecondary else SaffronPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
