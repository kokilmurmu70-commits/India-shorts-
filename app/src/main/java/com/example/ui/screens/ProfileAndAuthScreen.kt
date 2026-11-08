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
import com.example.data.DummyData
import com.example.model.BankVerificationStatus
import com.example.model.KycStatus
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun ProfileAndAuthScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isDark by viewModel.isDarkTheme.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val ownerKyc by viewModel.ownerKyc.collectAsState()
    val ownerWallet by viewModel.ownerWallet.collectAsState()
    val membershipState by viewModel.creatorMembership.collectAsState()

    var mobileInput by remember { mutableStateOf(currentUser.mobileNumber) }
    var otpInput by remember { mutableStateOf("984210") }

    var showAadhaarDialog by remember { mutableStateOf(false) }
    var showBankDialog by remember { mutableStateOf(false) }
    var showJoinSheet by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color(currentUser.avatarColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(currentUser.name.take(1), fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(currentUser.name, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                    if (currentUser.verifiedBadge) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = SaffronPrimary, modifier = Modifier.size(16.dp))
                                    }
                                }
                                Text(currentUser.username, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${currentUser.followersCount} Followers • ${currentUser.followingCount} Following", fontSize = 11.sp, color = SaffronPrimary)
                            }
                        }

                        if (currentUser.isOwner) {
                            Surface(color = EmeraldSecondary, shape = RoundedCornerShape(6.dp)) {
                                Text("FOUNDER", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(currentUser.bio, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 16.sp)

                    // REQUIREMENT 1: JOIN BUTTON ON CREATOR PROFILE (Only visible when Monetization is Approved)
                    if (!currentUser.isOwner) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Follow Button
                            Button(
                                onClick = { viewModel.toggleFollowCreator() },
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .testTag("btn_profile_follow")
                            ) {
                                Text("Follow", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            // JOIN Button - Strictly restricted to Monetization Approved creators (Rule 1 & 7)
                            if (currentUser.monetizationStatus == com.example.model.MonetizationStatus.MONETIZED) {
                                Button(
                                    onClick = { showJoinSheet = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (currentUser.isSubscribedToCreator) EmeraldSecondary else Color(0xFF0284C7)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .testTag("btn_profile_join_membership")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CardMembership,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (currentUser.isSubscribedToCreator) "MEMBER ✓" else "JOIN",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buttons 27, 28, 29
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { viewModel.executeButtonAction(27) },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_27")
                        ) {
                            Text("Btn 27: Edit Bio", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(28) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_28")
                        ) {
                            Text("Btn 28: Mode", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(29) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_29")
                        ) {
                            Text("Btn 29: Share", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        // OWNER KYC & ACCOUNT MANAGEMENT SECTION (Strictly private to Kokil Murmu)
        if (currentUser.isOwner) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("owner_kyc_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (ownerKyc.isKycApprovedForPayouts) EmeraldSecondary.copy(alpha = 0.5f) else AccentRed.copy(alpha = 0.5f)
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
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = if (ownerKyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Owner KYC & Account Vault",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary
                                )
                            }

                            Surface(
                                color = if (ownerKyc.isKycApprovedForPayouts) EmeraldSecondary.copy(alpha = 0.15f) else AccentRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (ownerKyc.isKycApprovedForPayouts) "KYC VERIFIED" else "KYC REQUIRED",
                                    color = if (ownerKyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Aadhaar Verification Status Box
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Aadhaar Identity Verification", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(
                                        text = ownerKyc.aadhaarStatus.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (ownerKyc.aadhaarStatus == KycStatus.VERIFIED) EmeraldSecondary else AccentRed
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Masked Aadhaar Number", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(
                                            text = ownerKyc.maskedAadhaar,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("UIDAI Verification Date", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(ownerKyc.verifiedDate, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Security: Salted SHA-256 Vault Encryption (Plaintext 12-digit number is never stored in database).",
                                    fontSize = 10.sp,
                                    color = TextMuted,
                                    lineHeight = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bank Account Verification Status Box (Punjab National Bank)
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Bank Account Verification", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(
                                        text = ownerKyc.bankVerificationStatus.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (ownerKyc.bankVerificationStatus == BankVerificationStatus.VERIFIED) EmeraldSecondary else AccentRed
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Designated Settlement Bank", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(ownerKyc.designatedBank, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Account: ${ownerKyc.maskedAccountNumber} • IFSC: ${ownerKyc.maskedIfscCode}", fontSize = 11.sp, color = SaffronPrimary)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Penny Drop Test", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(ownerKyc.pennyDropAmountFormatted, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldSecondary)
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Beneficiary Match: ${ownerKyc.beneficiaryName} (PNB Coordinates Vault Secured)",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Payout Eligibility Status Badge
                        Surface(
                            color = if (ownerKyc.isKycApprovedForPayouts) EmeraldSecondary.copy(alpha = 0.12f) else AccentRed.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (ownerKyc.isKycApprovedForPayouts) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (ownerKyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (ownerKyc.isKycApprovedForPayouts)
                                        "Payout Clearance: Eligible for Automatic Monthly 1st & Manual Disbursements."
                                    else
                                        "Payout Clearance: BLOCKED. Complete Aadhaar & Bank verification before payouts.",
                                    fontSize = 11.sp,
                                    color = if (ownerKyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action Buttons for Aadhaar & Bank Verification
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
                                    .height(38.dp)
                                    .testTag("btn_verify_aadhaar_kyc")
                            ) {
                                Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Aadhaar e-KYC", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { showBankDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .testTag("btn_verify_bank_account")
                            ) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Verify Bank", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.navigateTo(AppScreen.OWNER_WALLET) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .testTag("btn_goto_owner_payout_dash")
                            ) {
                                Text("Payout Dashboard", fontSize = 10.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    if (ownerKyc.isKycApprovedForPayouts) viewModel.resetOwnerKycForTesting()
                                    else viewModel.submitAadhaarKyc("123456787492", "984210")
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .testTag("btn_toggle_kyc_test_state")
                            ) {
                                Text(
                                    text = if (ownerKyc.isKycApprovedForPayouts) "Test KYC Lock" else "Quick Re-verify",
                                    fontSize = 10.sp,
                                    color = if (ownerKyc.isKycApprovedForPayouts) AccentRed else EmeraldSecondary
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // When logged in as Creator, enforce Requirement 5: Owner private information is completely hidden
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Creator Mode Active: Owner private KYC and financial records are protected & hidden.",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // Switch Active Test Profile
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Switch Active Test Profile", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DummyData.INITIAL_USERS.forEach { u ->
                            val isSel = u.id == currentUser.id
                            Surface(
                                color = if (isSel) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.switchUser(u.id) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        u.name.split(" ").first(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        if (u.isOwner) "Owner" else "Creator",
                                        fontSize = 9.sp,
                                        color = if (isSel) Color.White.copy(alpha = 0.8f) else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mobile Login & OTP Authentication (Label 30, 31 & Button 30, 31)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Label 30: Encrypted Mobile Login & OTP Protocol",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = mobileInput,
                        onValueChange = { mobileInput = it },
                        label = { Text("Registered Mobile Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { otpInput = it },
                            label = { Text("Enter OTP Code") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Button(
                            onClick = { viewModel.executeButtonAction(30) },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(52.dp)
                                .testTag("btn_30")
                        ) {
                            Text("Btn 30: Send OTP", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.executeButtonAction(31) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("btn_31")
                    ) {
                        Text("Btn 31: Verify OTP and Login", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.executeButtonAction(32) },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_32")
                        ) {
                            Text("Btn 32: Register", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.executeButtonAction(33) },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("btn_33")
                        ) {
                            Text("Btn 33: Recovery", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Settings & Privacy Controls (Label 38, 39 & Button 38, 39)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Label 38: System Theme & Digital Data Privacy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.toggleTheme() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("btn_38")
                        ) {
                            Icon(if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isDark) "Switch to Light" else "Switch to Dark", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(39) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("btn_39")
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Btn 39: Privacy Vault", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        // System Notifications & Alerts (Label 34, 35 & Button 34, 35)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Notifications & Alerts", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Label 34: Instant Push Activity Feed", fontSize = 11.sp, color = SaffronPrimary)
                }
                Row {
                    TextButton(onClick = { viewModel.markAllNotificationsRead() }, modifier = Modifier.testTag("btn_34")) {
                        Text("Btn 34: Mark Read", fontSize = 10.sp)
                    }
                    TextButton(onClick = { viewModel.clearAllNotifications() }, modifier = Modifier.testTag("btn_35")) {
                        Text("Btn 35: Clear", fontSize = 10.sp)
                    }
                }
            }
        }

        items(notifications) { notif ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("notif_card_${notif.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (notif.isRead) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (!notif.isRead) SaffronPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(notif.timeAgo, fontSize = 10.sp, color = TextMuted)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(notif.message, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
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
                        text = "Owner Name: Kokil Murmu\nEnter your 12-digit Aadhaar number and the UIDAI one-time authentication code (OTP):",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rawAadhaarInput,
                        onValueChange = {
                            rawAadhaarInput = it
                            errorMessage = null
                        },
                        label = { Text("12-Digit Aadhaar Number") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("aadhaar_number_input"),
                        singleLine = true,
                        placeholder = { Text("1234 5678 7492") }
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
                            .testTag("aadhaar_otp_input"),
                        singleLine = true,
                        placeholder = { Text("6-digit code") }
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(errorMessage ?: "", color = AccentRed, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🛡️ Security Standard: Your 12-digit Aadhaar number is hashed with salted SHA-256 and never saved in plain text. Only the masked number (XXXX XXXX ${rawAadhaarInput.filter { it.isDigit() }.takeLast(4)}) will be displayed.",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(8.dp),
                            lineHeight = 14.sp
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
                            errorMessage = "Please enter a valid 12-digit number and 6-digit OTP."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    modifier = Modifier.testTag("btn_confirm_aadhaar_kyc")
                ) {
                    Text("Verify with UIDAI Vault")
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
        var bankError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showBankDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = SaffronPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PNB Bank Account Verification", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Beneficiary: KOKIL MURMU\nBank: Punjab National Bank (PNB)\nInitiate automated penny drop (₹1.00 credit test) verification:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = ifscInput,
                        onValueChange = {
                            ifscInput = it
                            bankError = null
                        },
                        label = { Text("PNB Branch IFSC Code") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pnb_ifsc_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = accountLast4Input,
                        onValueChange = {
                            accountLast4Input = it
                            bankError = null
                        },
                        label = { Text("Account Number (Last 4 Digits)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pnb_account_last4_input"),
                        singleLine = true
                    )

                    if (bankError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(bankError ?: "", color = AccentRed, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🔒 Banking coordinates are stored in the backend vault with AES-256 encryption. Plaintext account numbers are never exposed in UI.",
                        fontSize = 10.sp,
                        color = TextMuted,
                        lineHeight = 14.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = viewModel.submitPnbBankVerification(ifscInput, accountLast4Input)
                        if (success) {
                            showBankDialog = false
                        } else {
                            bankError = "Please enter valid IFSC and Account digits."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary),
                    modifier = Modifier.testTag("btn_confirm_bank_verify")
                ) {
                    Text("Run Penny Drop Check")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBankDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showJoinSheet) {
        com.example.ui.components.MembershipJoinBottomSheet(
            creatorName = currentUser.name,
            creatorUsername = currentUser.username,
            plans = membershipState.plans,
            isAlreadySubscribed = currentUser.isSubscribedToCreator,
            activeTierName = currentUser.activeMemberTier?.displayName,
            onJoinPlanSelected = { plan ->
                viewModel.subscribeToCreatorMembership(plan)
            },
            onCancelMembership = {
                viewModel.cancelMemberSubscription()
            },
            onDismiss = { showJoinSheet = false }
        )
    }
}
