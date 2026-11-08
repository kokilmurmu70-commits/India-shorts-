package com.example.ui.screens

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BankVerificationStatus
import com.example.model.KycStatus
import com.example.model.TransactionStatus
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun OwnerWalletScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val wallet by viewModel.ownerWallet.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val kyc by viewModel.ownerKyc.collectAsState()

    var manualWithdrawInput by remember { mutableStateOf("2500") }
    var showWithdrawDialog by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }
    var showAadhaarDialog by remember { mutableStateOf(false) }
    var showBankDialog by remember { mutableStateOf(false) }
    var pendingActionAfterAuth by remember { mutableStateOf<(() -> Unit)?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // MANDATORY DEMO DATA / SIMULATION NOTICE BANNER
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("demo_mode_banner"),
                color = AccentGold.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Demo Notice",
                        tint = AccentGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "DEMO DATA / SIMULATION MODE",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AccentGold
                        )
                        Text(
                            text = "All balances, earnings, and payouts shown below are simulated demonstration data. Bank account numbers and IFSC codes are hidden and secured on backend servers only. Real disbursements require authenticated payment-provider integration.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Top Official Owner Header (Owner Name: Kokil Murmu, Bank: PNB)
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
                                listOf(SaffronDark, NavyDeep, NavySurface)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = SaffronPrimary,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "OFFICIAL OWNER DASHBOARD",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            // Owner Security Auth Status Chip
                            Surface(
                                color = if (wallet.isOwnerAuthenticated) EmeraldSecondary.copy(alpha = 0.2f) else AccentRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable {
                                    if (wallet.isOwnerAuthenticated) viewModel.lockOwnerVault()
                                    else showAuthDialog = true
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (wallet.isOwnerAuthenticated) Icons.Default.LockOpen else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (wallet.isOwnerAuthenticated) EmeraldSecondary else AccentRed,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (wallet.isOwnerAuthenticated) "Owner Authenticated" else "Locked (Tap to Authenticate)",
                                        color = if (wallet.isOwnerAuthenticated) EmeraldSecondary else AccentRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Platform Owner: ${wallet.ownerName}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Designated Bank: ${wallet.bankName}",
                            fontSize = 13.sp,
                            color = SaffronLight,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Banking Coordinates: Protected & Stored in Backend Vault Only",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Revenue Split Summary Pill (Mandatory: 60% Owner, 40% Creator Pool)
                        Surface(
                            color = Color.Black.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Owner Share (Kokil Murmu)", fontSize = 10.sp, color = TextSecondary)
                                    Text("60% (₹6,000 / ₹10,000)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SaffronLight)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Creator Pool (All Creators)", fontSize = 10.sp, color = TextSecondary)
                                    Text("40% (₹4,000 / ₹10,000)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldLight)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Owner KYC & Payout Eligibility Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("owner_wallet_kyc_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (kyc.isKycApprovedForPayouts) EmeraldSecondary.copy(alpha = 0.5f) else AccentRed.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = if (kyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Owner KYC & Payout Eligibility",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary.copy(alpha = 0.15f) else AccentRed.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (kyc.isKycApprovedForPayouts) "DISBURSEMENTS ACTIVE" else "PAYOUTS BLOCKED",
                                color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3 Verification Badges (Mobile, Aadhaar, Bank)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Mobile", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = kyc.mobileStatus.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (kyc.mobileStatus.name == "VERIFIED") EmeraldSecondary else AccentGold
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Aadhaar", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = if (kyc.status.name == "APPROVED") "Verified" else "Pending",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (kyc.status.name == "APPROVED") EmeraldSecondary else AccentGold
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Bank", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = if (kyc.bankVerificationStatus.name == "VERIFIED") "Verified" else "Pending",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (kyc.bankVerificationStatus.name == "VERIFIED") EmeraldSecondary else AccentGold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary.copy(alpha = 0.12f) else AccentRed.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (kyc.isKycApprovedForPayouts)
                                "✅ All 3 Verifications Complete: Mobile, Aadhaar & Bank verified. Payouts enabled."
                            else
                                "❌ Mandatory Rule: Mobile, Aadhaar & Bank verification must ALL be verified to withdraw funds.",
                            fontSize = 11.sp,
                            color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showAadhaarDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_wallet_aadhaar_kyc")
                        ) {
                            Text("Aadhaar KYC", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { showBankDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_wallet_verify_bank")
                        ) {
                            Text("Penny Drop Test", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        OutlinedButton(
                            onClick = {
                                if (kyc.isKycApprovedForPayouts) viewModel.resetOwnerKycForTesting()
                                else viewModel.submitAadhaarKyc("123456787492", "984210")
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_wallet_test_kyc_toggle")
                        ) {
                            Text(
                                text = if (kyc.isKycApprovedForPayouts) "Test Lock" else "Quick Verify",
                                fontSize = 10.sp,
                                color = if (kyc.isKycApprovedForPayouts) AccentRed else EmeraldSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.OWNER_KYC) },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .testTag("btn_wallet_open_dedicated_kyc")
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Full Owner KYC & Admin Vault", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Wallet Balance Metrics Card (All labeled DEMO DATA)
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
                            text = "Label 45: Owner Wallet Balances",
                            fontSize = 12.sp,
                            color = SaffronPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = AccentGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "DEMO DATA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${"%,.2f".format(wallet.availableBalanceUsd)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Available Finalized Balance (Demonstration Estimate)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Owner 60% Share (Verified)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("₹${"%,.0f".format(wallet.ownerShareInr)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                            }
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Creator 40% Pool (Verified)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("₹${"%,.0f".format(wallet.totalCreatorPoolInr)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EmeraldSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buttons 45 & 49 (Manual Withdrawal requiring Owner Auth)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (!wallet.isOwnerAuthenticated) {
                                    pendingActionAfterAuth = { showWithdrawDialog = true }
                                    showAuthDialog = true
                                } else {
                                    showWithdrawDialog = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("btn_49")
                        ) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Withdraw to PNB", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.executeButtonAction(46) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("btn_46")
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Btn 46: Statement", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Automatic Monthly Payout Engine Card (Monthly schedule kept, without fake verification)
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Automatic Monthly Payout Engine",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Payout Schedule: 1st of every month to PNB account",
                                fontSize = 12.sp,
                                color = SaffronPrimary
                            )
                        }
                        Switch(
                            checked = wallet.automaticMonthlyPayoutEnabled,
                            onCheckedChange = {
                                if (!wallet.isOwnerAuthenticated) {
                                    pendingActionAfterAuth = { viewModel.toggleOwnerAutoMonthlyPayout() }
                                    showAuthDialog = true
                                } else {
                                    viewModel.toggleOwnerAutoMonthlyPayout()
                                }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary),
                            modifier = Modifier.testTag("btn_47")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Next scheduled payout run: ${wallet.nextScheduledPayoutDate}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Bank Holiday Rule: Automatically shifts to next banking business day if 1st is a non-clearing holiday.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (!wallet.isOwnerAuthenticated) {
                                    pendingActionAfterAuth = { viewModel.simulateScheduled1stMonthlyPayoutCycle() }
                                    showAuthDialog = true
                                } else {
                                    viewModel.simulateScheduled1stMonthlyPayoutCycle()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_60")
                        ) {
                            Text("Btn 60: Simulate 1st Payout", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.retryFailedTransfer() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_50")
                        ) {
                            Text("Btn 50: Check Gateway Queue", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        // PNB Bank Account Details (Backend-only storage notice, NO account numbers or IFSC exposed)
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
                            text = "Label 51: Punjab National Bank Settlement Vault",
                            fontSize = 12.sp,
                            color = SaffronPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "BACKEND SECURED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Banking Privacy & Zero-Exposure Standard", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Destination Bank: Punjab National Bank (PNB)\n• Account & IFSC coordinates are stored strictly on backend servers with AES-256 encryption and are never exposed in the client application UI.\n• Gateway Status: Awaiting authorized banking provider API integration for real-time verification.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.verifyPnbBankAccount() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_51")
                        ) {
                            Text("Btn 51: Query Backend Vault", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        OutlinedButton(
                            onClick = { viewModel.executeButtonAction(52) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_52")
                        ) {
                            Text("Btn 52: Update Coordinates", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // PNB Bank Settlement History (Never showing fake successful withdrawals without real provider confirmation)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Settlement Queue & Audit Ledger",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    color = AccentGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "DEMO SIMULATION",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }

        items(transactions) { txn ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("txn_card_${txn.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${txn.amountFormatted} (DEMO)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = txn.destinationAccount,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Reference: ${txn.txnId}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                        Text(
                            text = txn.note,
                            fontSize = 10.sp,
                            color = SaffronPrimary,
                            lineHeight = 13.sp
                        )
                    }

                    Surface(
                        color = when (txn.status) {
                            TransactionStatus.PROCESSING -> AccentGold.copy(alpha = 0.2f)
                            TransactionStatus.PENDING -> AccentGold.copy(alpha = 0.2f)
                            TransactionStatus.FAILED -> AccentRed.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = when (txn.status) {
                                TransactionStatus.PROCESSING -> "PROCESSING GATEWAY"
                                TransactionStatus.PENDING -> "PENDING CONFIRMATION"
                                TransactionStatus.FAILED -> "FAILED"
                                else -> "DEMO PENDING"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (txn.status) {
                                TransactionStatus.PROCESSING -> AccentGold
                                TransactionStatus.PENDING -> AccentGold
                                TransactionStatus.FAILED -> AccentRed
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Owner Security Authentication Dialog (Required before withdrawals)
    if (showAuthDialog) {
        var ownerPinInput by remember { mutableStateOf("") }
        var authError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = {
                showAuthDialog = false
                pendingActionAfterAuth = null
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = SaffronPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Owner Security Authentication", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Owner: Kokil Murmu\nTo access financial withdrawal controls, enter your Owner Security PIN:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = ownerPinInput,
                        onValueChange = {
                            ownerPinInput = it
                            authError = false
                        },
                        label = { Text("Security PIN (e.g. 1234)") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("owner_security_pin_input"),
                        singleLine = true,
                        isError = authError
                    )
                    if (authError) {
                        Text("Please enter a valid PIN (minimum 4 digits)", color = AccentRed, fontSize = 11.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = viewModel.authenticateOwner(ownerPinInput)
                        if (success) {
                            showAuthDialog = false
                            val action = pendingActionAfterAuth
                            pendingActionAfterAuth = null
                            action?.invoke()
                        } else {
                            authError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text("Verify & Unlock")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAuthDialog = false
                    pendingActionAfterAuth = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Manual Withdrawal Amount Dialog
    if (showWithdrawDialog) {
        AlertDialog(
            onDismissRequest = { showWithdrawDialog = false },
            title = {
                Text("Manual PNB Bank Withdrawal (Demo Mode)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    Text(
                        text = "Destination: Punjab National Bank (Owner: Kokil Murmu)\nAccount coordinates are secured on backend server only.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (!kyc.isKycApprovedForPayouts) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = AccentRed.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "❌ DISBURSEMENT BLOCKED: Aadhaar e-KYC and PNB Bank account verification must be verified before any manual or scheduled payouts can be issued.",
                                color = AccentRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = manualWithdrawInput,
                        onValueChange = { manualWithdrawInput = it },
                        label = { Text("Amount in USD ($)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("manual_withdraw_amount_input"),
                        singleLine = true,
                        enabled = kyc.isKycApprovedForPayouts
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Available finalized balance (DEMO): $${"%,.2f".format(wallet.availableBalanceUsd)}",
                        fontSize = 11.sp,
                        color = SaffronPrimary
                    )
                    Text(
                        text = "Notice: This initiates a simulated withdrawal request. Real payout requires payment provider confirmation.",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            },
            confirmButton = {
                if (kyc.isKycApprovedForPayouts) {
                    Button(
                        onClick = {
                            val amt = manualWithdrawInput.toDoubleOrNull() ?: 0.0
                            viewModel.executeOwnerManualWithdrawal(amt)
                            showWithdrawDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                    ) {
                        Text("Queue Withdrawal Request")
                    }
                } else {
                    Button(
                        onClick = {
                            showWithdrawDialog = false
                            showAadhaarDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                    ) {
                        Text("Complete KYC First")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // AADHAAR e-KYC VERIFICATION DIALOG
    if (showAadhaarDialog) {
        var rawAadhaarInput by remember { mutableStateOf("123456787492") }
        var kycOtpInput by remember { mutableStateOf("984210") }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showAadhaarDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, tint = SaffronPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Aadhaar e-KYC Verification", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Owner Name: Kokil Murmu\nEnter your 12-digit Aadhaar number and UIDAI OTP:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = rawAadhaarInput,
                        onValueChange = {
                            rawAadhaarInput = it
                            errorMessage = null
                        },
                        label = { Text("12-Digit Aadhaar Number") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("owner_wallet_aadhaar_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = kycOtpInput,
                        onValueChange = {
                            kycOtpInput = it
                            errorMessage = null
                        },
                        label = { Text("UIDAI OTP Code") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("owner_wallet_aadhaar_otp"),
                        singleLine = true
                    )
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(errorMessage ?: "", color = AccentRed, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🛡️ Plaintext Aadhaar is never saved in database. Only salted SHA-256 hash and masked digits (XXXX XXXX ${rawAadhaarInput.filter { it.isDigit() }.takeLast(4)}) are stored.",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = viewModel.submitAadhaarKyc(rawAadhaarInput, kycOtpInput)
                        if (success) {
                            showAadhaarDialog = false
                        } else {
                            errorMessage = "Please enter valid 12-digit number and 6-digit OTP."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text("Authenticate & Hash")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAadhaarDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // BANK ACCOUNT VERIFICATION DIALOG (PUNJAB NATIONAL BANK)
    if (showBankDialog) {
        var ifscInput by remember { mutableStateOf("PUNB0123400") }
        var accountLast4Input by remember { mutableStateOf("4018") }

        AlertDialog(
            onDismissRequest = { showBankDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = SaffronPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PNB Bank Penny Drop Test", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Beneficiary: KOKIL MURMU\nBank: Punjab National Bank (PNB)\nConfirm IFSC and account details to run automated penny drop verification:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = ifscInput,
                        onValueChange = { ifscInput = it },
                        label = { Text("PNB Branch IFSC Code") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = accountLast4Input,
                        onValueChange = { accountLast4Input = it },
                        label = { Text("Account Last 4 Digits") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitPnbBankVerification(ifscInput, accountLast4Input)
                        showBankDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary)
                ) {
                    Text("Verify ₹1.00 Penny Drop")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBankDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
