package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BankVerificationStatus
import com.example.model.MobileVerificationStatus
import com.example.model.OwnerKycStatus
import com.example.model.TransactionType
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun OwnerKycScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.OWNER_WALLET)
    }

    val currentUser by viewModel.currentUser.collectAsState()
    val wallet by viewModel.ownerWallet.collectAsState()
    val kyc by viewModel.ownerKyc.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    var showPinDialog by remember { mutableStateOf(false) }
    var mobileInput by remember { mutableStateOf("9876543210") }
    var mobileOtpInput by remember { mutableStateOf("654321") }
    var rawAadhaarInput by remember { mutableStateOf("123456787492") }
    var otpInput by remember { mutableStateOf("984210") }
    var accountNumberInput by remember { mutableStateOf("1234567890124018") }
    var ifscInput by remember { mutableStateOf("PUNB0123400") }
    var accountHolderNameInput by remember { mutableStateOf("KOKIL MURMU") }
    var adminNoteInput by remember { mutableStateOf("Approved by Master Admin following Mobile, Aadhaar & Banking clearance") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // REQUIREMENT 1 & 2: EXCLUSIVELY FOR OWNER. CREATORS DO NOT REQUIRE KYC.
        if (!currentUser.isOwner) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("owner_kyc_access_denied_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(AccentRed.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = AccentRed, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Owner-Only Section Restricted",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This KYC Verification Dashboard is exclusively reserved for the INDIA SHORTS App Owner (Kokil Murmu).\n\nCreators are NOT required to submit KYC verification and have complete access to upload videos, earn view-based revenue, and use the Creator Studio.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 17.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.SHORTS) },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            modifier = Modifier.testTag("btn_return_from_kyc_denied")
                        ) {
                            Text("Return to Shorts Feed")
                        }
                    }
                }
            }
            return@LazyColumn
        }

        // TOP HEADER
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.linearGradient(listOf(SaffronDark, NavyDeep, NavySurface)))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(color = SaffronPrimary, shape = RoundedCornerShape(6.dp)) {
                                Text(
                                    text = "OWNER-ONLY KYC DASHBOARD",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Surface(
                                color = if (wallet.isOwnerAuthenticated) EmeraldSecondary.copy(alpha = 0.2f) else AccentRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable {
                                    if (wallet.isOwnerAuthenticated) viewModel.lockOwnerVault()
                                    else showPinDialog = true
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
                                        text = if (wallet.isOwnerAuthenticated) "Vault Unlocked" else "Locked (Tap to Unlock)",
                                        color = if (wallet.isOwnerAuthenticated) EmeraldSecondary else AccentRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Platform Owner: Kokil Murmu",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Authorized KYC & Bank Account Settlement Vault",
                            fontSize = 12.sp,
                            color = SaffronLight
                        )
                        Text(
                            text = "Zero Exposure Policy: Plaintext Aadhaar or OTP is never stored in database or shared with Creators.",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // REQUIREMENT 4: OWNER DASHBOARD - SHOW ALL 3 VERIFICATION STATUSES
        // - Mobile: Pending / Verified
        // - Aadhaar: Pending / Verified
        // - Bank: Pending / Verified
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("owner_kyc_master_status_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (kyc.mobileStatus == MobileVerificationStatus.VERIFIED &&
                        kyc.status == OwnerKycStatus.APPROVED &&
                        kyc.bankVerificationStatus == BankVerificationStatus.VERIFIED) {
                        EmeraldSecondary
                    } else AccentGold
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Owner Tri-Verification Dashboard",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Exclusively for India Shorts Owner (Kokil Murmu)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary.copy(alpha = 0.18f) else AccentRed.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (kyc.isKycApprovedForPayouts) "ALL 3 VERIFIED" else "PENDING COMPLETION",
                                color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Verification Badges Row (Mobile, Aadhaar, Bank)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Mobile Status
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("status_badge_mobile")
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.PhoneAndroid,
                                    contentDescription = null,
                                    tint = if (kyc.mobileStatus == MobileVerificationStatus.VERIFIED) EmeraldSecondary else AccentGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("1. Mobile", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Surface(
                                    color = if (kyc.mobileStatus == MobileVerificationStatus.VERIFIED) EmeraldSecondary.copy(alpha = 0.2f) else AccentGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = kyc.mobileStatus.label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (kyc.mobileStatus == MobileVerificationStatus.VERIFIED) EmeraldSecondary else AccentGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // 2. Aadhaar Status
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("status_badge_aadhaar")
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    tint = if (kyc.status == OwnerKycStatus.APPROVED) EmeraldSecondary else AccentGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("2. Aadhaar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Surface(
                                    color = if (kyc.status == OwnerKycStatus.APPROVED) EmeraldSecondary.copy(alpha = 0.2f) else AccentGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (kyc.status == OwnerKycStatus.APPROVED) "Verified" else "Pending",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (kyc.status == OwnerKycStatus.APPROVED) EmeraldSecondary else AccentGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // 3. Bank Status
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("status_badge_bank")
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = if (kyc.bankVerificationStatus == BankVerificationStatus.VERIFIED) EmeraldSecondary else AccentGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("3. Bank", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Surface(
                                    color = if (kyc.bankVerificationStatus == BankVerificationStatus.VERIFIED) EmeraldSecondary.copy(alpha = 0.2f) else AccentGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (kyc.bankVerificationStatus == BankVerificationStatus.VERIFIED) "Verified" else "Pending",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (kyc.bankVerificationStatus == BankVerificationStatus.VERIFIED) EmeraldSecondary else AccentGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Withdrawal Clearance Rule (Rule 5 & 11)
                    Surface(
                        color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary.copy(alpha = 0.12f) else AccentRed.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (kyc.isKycApprovedForPayouts) Icons.Default.CheckCircle else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (kyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (kyc.isKycApprovedForPayouts)
                                    "Owner Withdrawal System: ENABLED & ACTIVE. (Mobile, Aadhaar & Bank all verified)."
                                else
                                    "Owner Withdrawal System: STRICTLY BLOCKED. Mobile, Aadhaar & Bank verification must ALL be verified.",
                                fontSize = 11.sp,
                                color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // REQUIREMENT 1: OWNER MOBILE NUMBER VERIFICATION MODULE (Authorized SMS Gateway)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("owner_kyc_mobile_module_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("1. Mobile Number Verification", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Surface(
                            color = if (kyc.mobileStatus == MobileVerificationStatus.VERIFIED) EmeraldSecondary.copy(alpha = 0.15f) else AccentGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = kyc.mobileStatus.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (kyc.mobileStatus == MobileVerificationStatus.VERIFIED) EmeraldSecondary else AccentGold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Authorized Telecom Provider: ${kyc.authorizedSmsProviderName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Masked Mobile Number: ${kyc.maskedMobileNumber}", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = SaffronPrimary)
                            if (kyc.mobileStatus == MobileVerificationStatus.VERIFIED) {
                                Text("Verified on: ${kyc.mobileVerifiedDate} (TRAI DLT Template Certified)", fontSize = 10.sp, color = TextMuted)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!kyc.isMobileOtpRequested) {
                        OutlinedTextField(
                            value = mobileInput,
                            onValueChange = { mobileInput = it },
                            label = { Text("Enter 10-Digit Owner Mobile Number") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_owner_mobile_digits"),
                            singleLine = true,
                            placeholder = { Text("9876543210") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { viewModel.requestOwnerMobileOtp(mobileInput) },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("btn_request_mobile_otp")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send OTP via Authorized SMS Gateway", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            color = AccentGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "OTP Sent to ${kyc.maskedMobileNumber} via Authorized SMS Gateway.\nEnter the 6-digit code:",
                                fontSize = 11.sp,
                                color = AccentGold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = mobileOtpInput,
                            onValueChange = { mobileOtpInput = it },
                            label = { Text("Enter 6-Digit Mobile SMS OTP") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_owner_mobile_otp"),
                            singleLine = true,
                            placeholder = { Text("654321") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.submitOwnerMobileOtp(mobileOtpInput) },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .testTag("btn_submit_mobile_otp")
                            ) {
                                Text("Verify Mobile OTP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.requestOwnerMobileOtp(mobileInput) },
                                modifier = Modifier
                                    .weight(0.7f)
                                    .height(40.dp)
                                    .testTag("btn_resend_mobile_otp")
                            ) {
                                Text("Resend SMS", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // REQUIREMENT 4, 5, 6, 7: AADHAAR e-KYC VERIFICATION MODULE (Authorized Provider Flow)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("owner_kyc_aadhaar_module_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Authorized Aadhaar e-KYC Module", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Displayed Masked Aadhaar", fontSize = 10.sp, color = TextMuted)
                                Text(kyc.maskedAadhaar, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = SaffronPrimary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Beneficiary Name: ${kyc.fullNameAsPerAadhaar}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("UIDAI Verification Date: ${kyc.verifiedDate}", fontSize = 10.sp, color = TextMuted)
                            Text("Vault Hash: ${kyc.aadhaarHashSha256.take(24)}... (Salted SHA-256)", fontSize = 9.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Two-Step Provider Authentication Flow:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    if (!kyc.isOtpRequested) {
                        // Step 1: Request OTP through Authorized Provider
                        OutlinedTextField(
                            value = rawAadhaarInput,
                            onValueChange = { rawAadhaarInput = it },
                            label = { Text("12-Digit Aadhaar Number") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_provider_aadhaar_digits"),
                            singleLine = true,
                            placeholder = { Text("1234 5678 7492") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { viewModel.requestProviderAadhaarOtp(rawAadhaarInput) },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("btn_request_provider_otp")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Request OTP via Authorized Provider Gateway", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Step 2: Submit OTP to Authorized Provider
                        Surface(
                            color = AccentGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Provider Session Active: ${kyc.providerSessionId}\nEnter the 6-digit verification code:",
                                fontSize = 11.sp,
                                color = AccentGold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { otpInput = it },
                            label = { Text("Enter 6-Digit Provider OTP") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_provider_otp"),
                            singleLine = true,
                            placeholder = { Text("984210") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.submitProviderAadhaarOtp(otpInput) },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .testTag("btn_submit_provider_otp")
                            ) {
                                Text("Verify Provider OTP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.requestProviderAadhaarOtp(rawAadhaarInput) },
                                modifier = Modifier
                                    .weight(0.7f)
                                    .height(40.dp)
                                    .testTag("btn_resend_provider_otp")
                            ) {
                                Text("Resend OTP", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // REQUIREMENT 3: OWNER BANK ACCOUNT VERIFICATION (Authorized Banking Gateway + Penny Drop)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("owner_kyc_bank_module_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("3. Bank Account Verification", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Surface(
                            color = if (kyc.bankVerificationStatus == BankVerificationStatus.VERIFIED) EmeraldSecondary.copy(alpha = 0.15f) else AccentGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = kyc.bankVerificationStatus.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (kyc.bankVerificationStatus == BankVerificationStatus.VERIFIED) EmeraldSecondary else AccentGold,
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
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Banking Gateway: ${kyc.authorizedBankingGatewayName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Bank Name: ${kyc.designatedBank}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Verified Account Holder: ${kyc.bankAccountHolderVerifiedName} (Name Matched)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldSecondary)
                            Text("Masked Account: ${kyc.maskedAccountNumber} • IFSC: ${kyc.maskedIfscCode}", fontSize = 11.sp, color = SaffronPrimary)
                            Text("Penny Drop Test: ${kyc.pennyDropAmountFormatted} (Ref: ${kyc.pennyDropRefId})", fontSize = 10.sp, color = TextMuted)
                            Text("Security Notice: Full bank details stored in backend secure vault only.", fontSize = 9.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = accountNumberInput,
                        onValueChange = { accountNumberInput = it },
                        label = { Text("Bank Account Number (9–18 Digits)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_owner_bank_account"),
                        singleLine = true,
                        placeholder = { Text("1234567890124018") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ifscInput,
                            onValueChange = { ifscInput = it },
                            label = { Text("Bank IFSC Code") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_owner_bank_ifsc"),
                            singleLine = true,
                            placeholder = { Text("PUNB0123400") }
                        )
                        OutlinedTextField(
                            value = accountHolderNameInput,
                            onValueChange = { accountHolderNameInput = it },
                            label = { Text("Account Holder Name") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_owner_bank_holder"),
                            singleLine = true,
                            placeholder = { Text("KOKIL MURMU") }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.submitAuthorizedBankAccountVerification(accountNumberInput, ifscInput, accountHolderNameInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("btn_run_penny_drop_verification")
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Verify Bank Account via Penny Drop Gateway", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // REQUIREMENT 10: SECURE ADMIN VERIFICATION CONTROLS
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_verification_controls_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Secure Admin Verification Controls", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Master Admin review controls to audit, approve, or reject Owner KYC status:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = adminNoteInput,
                        onValueChange = { adminNoteInput = it },
                        label = { Text("Admin Audit / Approval Note") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.adminApproveOwnerKyc(adminNoteInput) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_admin_approve_kyc")
                        ) {
                            Text("Admin Approve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.adminRejectOwnerKyc("Rejected by Admin: " + adminNoteInput) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_admin_reject_kyc")
                        ) {
                            Text("Admin Reject", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.resetOwnerKycForTesting() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(0.9f)
                                .height(38.dp)
                                .testTag("btn_admin_reset_kyc")
                        ) {
                            Text("Set Pending", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // REQUIREMENT 12: MONTHLY AUTOMATIC PAYOUT MANAGEMENT
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("monthly_payout_management_card"),
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
                            Text("Monthly Automatic Payout Engine", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Schedule: 1st of every month to PNB account", fontSize = 11.sp, color = SaffronPrimary)
                        }

                        Switch(
                            checked = wallet.automaticMonthlyPayoutEnabled,
                            onCheckedChange = { viewModel.toggleOwnerAutoMonthlyPayout() },
                            colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary),
                            modifier = Modifier.testTag("switch_owner_auto_payout")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Next Scheduled Settlement: ${wallet.nextScheduledPayoutDate}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Disbursement Status: ${if (kyc.isKycApprovedForPayouts) "CLEAR TO DISBURSE" else "HOLD - KYC PENDING"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (kyc.isKycApprovedForPayouts) EmeraldSecondary else AccentRed
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.OWNER_WALLET) },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_goto_owner_wallet_from_kyc")
                        ) {
                            Text("Owner Wallet & Payout Terminal", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.retryBackendKycConnection() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(0.7f)
                                .height(38.dp)
                                .testTag("btn_test_gateway_connection")
                        ) {
                            Text("Test Gateway", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // AUDIT TRAIL / RECENT KYC TRANSACTIONS
        item {
            Text("KYC Audit Ledger & Cryptographic Signatures", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            val kycTxns = transactions.filter {
                it.type == TransactionType.OWNER_KYC_VERIFICATION || it.type == TransactionType.OWNER_BANK_VERIFICATION
            }
            if (kycTxns.isEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("No KYC audit transactions recorded yet.", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(12.dp))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    kycTxns.take(5).forEach { txn ->
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(txn.amountFormatted, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SaffronPrimary)
                                    Text(txn.timestamp, fontSize = 10.sp, color = TextMuted)
                                }
                                Text("Destination: ${txn.destinationAccount}", fontSize = 11.sp)
                                Text("Ref: ${txn.txnId}", fontSize = 10.sp, color = TextMuted)
                                Text(txn.note, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Owner Security Authentication Dialog
    if (showPinDialog) {
        var ownerPinInput by remember { mutableStateOf("") }
        var authError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("Owner Security Authentication", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter your Owner Security PIN (e.g. 1234):", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = ownerPinInput,
                        onValueChange = {
                            ownerPinInput = it
                            authError = false
                        },
                        label = { Text("Security PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("kyc_pin_input"),
                        singleLine = true,
                        isError = authError
                    )
                    if (authError) {
                        Text("Invalid PIN. Enter at least 4 digits.", color = AccentRed, fontSize = 11.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = viewModel.authenticateOwner(ownerPinInput)
                        if (success) {
                            showPinDialog = false
                        } else {
                            authError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text("Unlock Vault")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
