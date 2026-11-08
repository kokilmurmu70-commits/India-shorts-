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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MembershipPlan
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipJoinBottomSheet(
    creatorName: String,
    creatorUsername: String,
    plans: List<MembershipPlan>,
    isAlreadySubscribed: Boolean,
    activeTierName: String?,
    onJoinPlanSelected: (MembershipPlan) -> Unit,
    onCancelMembership: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPlan by remember { mutableStateOf(plans.firstOrNull()) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.testTag("membership_join_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header: Creator details & JOIN badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(SaffronPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = creatorName.take(1),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Join $creatorName",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = EmeraldSecondary.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Monetized Partner",
                                    color = EmeraldSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = creatorUsername,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mandatory Transparency Notice (Rule 7)
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "ℹ️ Voluntary Creator Support • Cancel Anytime",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    Text(
                        text = "Joining is completely optional. Subscriptions renew monthly with clear terms. 80% goes directly to the creator. No fake claims.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isAlreadySubscribed) {
                // Active Member status card with Cancel Membership option
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldSecondary.copy(alpha = 0.12f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSecondary)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "You are an Active Member ($activeTierName)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = EmeraldSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Special Member Badge is active next to your comments and username.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = onCancelMembership,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentRed),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .testTag("btn_cancel_membership")
                        ) {
                            Text("Cancel Membership Subscription", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            Text("Select a Membership Level:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(plans) { plan ->
                    val isSelected = selectedPlan?.id == plan.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPlan = plan }
                            .testTag("plan_card_${plan.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) SaffronPrimary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Color(plan.level.badgeColorHex)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(plan.level.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }

                                Text(
                                    text = "₹${"%.0f".format(plan.monthlyPriceInr)} / mo",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SaffronPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Perks list (Exclusive Videos, Community, Early Access, Badge)
                            plan.perks.forEach { perk ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldSecondary,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = perk,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Button: JOIN with authorized payment flow
            val chosen = selectedPlan ?: plans.firstOrNull()
            Button(
                onClick = {
                    if (chosen != null) {
                        showConfirmationDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("btn_confirm_join_plan")
            ) {
                Icon(Icons.Default.CardMembership, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (chosen != null) "JOIN for ₹${"%.0f".format(chosen.monthlyPriceInr)} / month" else "Select a Plan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Payment Gateway Confirmation Dialog
    if (showConfirmationDialog && selectedPlan != null) {
        val plan = selectedPlan!!
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Authorized Payment Gateway", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "You are subscribing to ${plan.creatorName}'s ${plan.level.displayName}.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Monthly Membership Fee:", fontSize = 11.sp)
                                Text("₹${"%.0f".format(plan.monthlyPriceInr)}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SaffronPrimary)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Payment Mode:", fontSize = 10.sp, color = TextMuted)
                                Text("UPI / NetBanking / Debit Card", fontSize = 10.sp, color = TextMuted)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Billing Frequency:", fontSize = 10.sp, color = TextMuted)
                                Text("Monthly Recurring (Cancel Anytime)", fontSize = 10.sp, color = TextMuted)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "By proceeding, you agree to INDIA SHORTS Membership Terms. 80% goes directly to the creator.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        onJoinPlanSelected(plan)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary),
                    modifier = Modifier.testTag("btn_gateway_pay_now")
                ) {
                    Text("Pay ₹${"%.0f".format(plan.monthlyPriceInr)} & Join", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
