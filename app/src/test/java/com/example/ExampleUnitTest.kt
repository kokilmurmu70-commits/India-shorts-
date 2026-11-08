package com.example

import com.example.data.ControlsCatalog
import com.example.data.DummyData
import com.example.model.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testExactly60ControlsExist() {
        val controls = ControlsCatalog.ALL_60_CONTROLS
        assertEquals("Controls must contain exactly 60 elements", 60, controls.size)

        val uniqueLabels = controls.map { it.labelName }.distinct()
        assertEquals("All 60 labels must be unique", 60, uniqueLabels.size)

        val uniqueButtons = controls.map { it.buttonName }.distinct()
        assertEquals("All 60 buttons must be unique", 60, uniqueButtons.size)

        // Verify IDs are sequential 1 to 60
        controls.forEachIndexed { index, item ->
            assertEquals(index + 1, item.id)
            assertTrue("Label name cannot be blank", item.labelName.isNotBlank())
            assertTrue("Button name cannot be blank", item.buttonName.isNotBlank())
            assertTrue("Action ID cannot be blank", item.actionId.isNotBlank())
            assertTrue("Domain cannot be blank", item.domain.isNotBlank())
        }
    }

    @Test
    fun testRevenueSharingFormula() {
        // MANDATORY RULE: 40% Creator Pool, 60% Owner Share
        val totalVerifiedAdsRevenue = 10_000.0
        val creatorPool = totalVerifiedAdsRevenue * 0.40 // 40% = ₹4,000
        val ownerShare = totalVerifiedAdsRevenue * 0.60  // 60% = ₹6,000

        assertEquals(4000.0, creatorPool, 0.001)
        assertEquals(6000.0, ownerShare, 0.001)
        assertEquals(totalVerifiedAdsRevenue, creatorPool + ownerShare, 0.001)

        // Creator Proportional Calculation Test:
        // Creator Earning = (Total Creator Pool * Creator Eligible Views) / All Creators' Eligible Views
        val allCreatorsViews = 100_000L
        val creatorAViews = 10_000L
        val creatorAEarning = (creatorPool * creatorAViews) / allCreatorsViews.toDouble()

        assertEquals(400.0, creatorAEarning, 0.001)
    }

    @Test
    fun testAdMobTestConfiguration() {
        val config = AdMobConfig()
        assertTrue(config.isTestMode)
        assertTrue(config.antiBotClickProtectionEnabled)
        assertEquals("ca-app-pub-3940256099942544~3347511713", config.appId)
        assertEquals("ca-app-pub-3940256099942544/6300978111", config.bannerAdUnitId)
        assertEquals("ca-app-pub-3940256099942544/2247696110", config.nativeAdUnitId)
    }

    @Test
    fun testOwnerInformationAndBank() {
        val wallet = OwnerWalletState()
        assertEquals("KOKIL MURMU", wallet.ownerName)
        assertEquals("PUNJAB NATIONAL BANK (PNB)", wallet.bankName)
        assertTrue(wallet.isDemoMode)
        assertTrue(wallet.automaticMonthlyPayoutEnabled)
    }

    @Test
    fun testCreatorMonetizationThresholds() {
        val m = MonetizationProgress()
        assertEquals(500, m.followerTarget)
        assertEquals(2000, m.watchHoursTarget)
        assertEquals(100000L, m.shortsViewsTarget)
        assertEquals(200.0, m.minimumWithdrawalUsd, 0.01)
    }

    @Test
    fun testSpecialCreatorReward() {
        val reward = SpecialRewardState()
        assertEquals(1_100_000_000L, reward.targetViews) // 1.10 Billion views
        assertEquals(1_000_000L, reward.rewardAmountInr) // ₹10,00,000 (10 Lakh)
        assertTrue(reward.isApprovedAndFunded)
    }

    @Test
    fun test20SupportedLanguages() {
        val languages = DummyData.SUPPORTED_LANGUAGES
        assertTrue("At least 20 languages supported", languages.size >= 20)
        val codes = languages.map { it.code }
        assertTrue(codes.contains("sat")) // Santali
        assertTrue(codes.contains("khr")) // Khortha
        assertTrue(codes.contains("hi"))  // Hindi
        assertTrue(codes.contains("en"))  // English
        assertTrue(codes.contains("te"))  // Telugu
        assertTrue(codes.contains("ta"))  // Tamil
        assertTrue(codes.contains("bho")) // Bhojpuri
    }

    @Test
    fun testUploadVideoCategoriesAndFormats() {
        // Both Shorts and Long videos must be supported
        val types = VideoType.values().toList()
        assertTrue(types.contains(VideoType.SHORT))
        assertTrue(types.contains(VideoType.LONG))

        // All 8 requested categories must be present
        val categories = VideoCategory.values().map { it.displayName }
        assertTrue(categories.contains("Comedy"))
        assertTrue(categories.contains("Village Vlog"))
        assertTrue(categories.contains("Educational"))
        assertTrue(categories.contains("Cultural"))
        assertTrue(categories.contains("Entertainment"))
        assertTrue(categories.contains("Music"))
        assertTrue(categories.contains("Travel"))
        assertTrue(categories.contains("Original"))
    }

    @Test
    fun testUploadSystemStateAndCancellation() {
        val vm = IndiaShortsViewModel()

        // Verify clean initial state: No fake sample videos populated by default
        assertNull(vm.selectedVideoUri.value)
        assertNull(vm.selectedVideoName.value)
        assertNull(vm.selectedVideoDurationText.value)

        // Test video and thumbnail selection
        vm.setSelectedVideo("content://media/external/video/media/101", "my_village_shot.mp4")
        assertEquals("content://media/external/video/media/101", vm.selectedVideoUri.value)
        assertEquals("my_village_shot.mp4", vm.selectedVideoName.value)

        vm.setSelectedThumbnail("content://media/external/images/media/202")
        assertEquals("content://media/external/images/media/202", vm.selectedThumbnailUri.value)

        // Test video format switching
        vm.setUploadVideoType(VideoType.LONG)
        assertEquals(VideoType.LONG, vm.selectedUploadVideoType.value)
        vm.setUploadVideoType(VideoType.SHORT)
        assertEquals(VideoType.SHORT, vm.selectedUploadVideoType.value)

        // Test preview playback toggle
        assertFalse(vm.isPreviewPlaying.value)
        vm.togglePreviewPlayback()
        assertTrue(vm.isPreviewPlaying.value)

        // Test cancellation
        vm.cancelUpload()
        assertFalse(vm.isUploading.value)
        assertEquals(0, vm.uploadProgress.value)

        // Test Demo video loader
        vm.loadDemoVideo()
        assertTrue(vm.isDemoVideoLoaded.value)
        assertNotNull(vm.selectedVideoUri.value)
        assertEquals("india_shorts_santhal_harvest_vlog.mp4", vm.selectedVideoName.value)
        assertEquals("0:45", vm.selectedVideoDurationText.value)
        assertEquals("14.8 MB", vm.selectedVideoSizeText.value)

        // Test video selection error handling
        vm.setVideoSelectionError("Unsupported video format")
        assertEquals("Unsupported video format", vm.videoSelectionError.value)

        // Test clear video selection
        vm.clearVideoSelection()
        assertNull(vm.selectedVideoUri.value)
        assertNull(vm.selectedVideoName.value)
        assertNull(vm.selectedVideoDurationText.value)
        assertFalse(vm.isDemoVideoLoaded.value)
    }

    @Test
    fun testOwnerKycAndAadhaarMasking() {
        val vm = IndiaShortsViewModel()
        val initialKyc = vm.ownerKyc.value

        // Initial KYC state must be verified with masked Aadhaar
        assertEquals(KycStatus.VERIFIED, initialKyc.aadhaarStatus)
        assertEquals("XXXX XXXX 7492", initialKyc.maskedAadhaar)
        assertTrue(initialKyc.isKycApprovedForPayouts)
        assertTrue(initialKyc.aadhaarHashSha256.isNotBlank())
        // Plaintext 12-digit number must never be in state
        assertFalse(initialKyc.aadhaarHashSha256.contains("1234"))

        // Test Aadhaar submission with 12 digits
        val rawInput = "987654321098"
        val success = vm.submitAadhaarKyc(rawInput, "984210")
        assertTrue(success)

        val updatedKyc = vm.ownerKyc.value
        assertEquals(KycStatus.VERIFIED, updatedKyc.aadhaarStatus)
        // Must show only masked number with last 4 digits
        assertEquals("XXXX XXXX 1098", updatedKyc.maskedAadhaar)
        // Never stored in plain text: state must not contain rawInput
        assertNotEquals(rawInput, updatedKyc.aadhaarHashSha256)
        assertEquals(64, updatedKyc.aadhaarHashSha256.length) // Standard SHA-256 hex length
    }

    @Test
    fun testOwnerPnbBankVerification() {
        val vm = IndiaShortsViewModel()

        val verified = vm.submitPnbBankVerification("PUNB0998800", "7733")
        assertTrue(verified)

        val kyc = vm.ownerKyc.value
        assertEquals(BankVerificationStatus.VERIFIED, kyc.bankVerificationStatus)
        assertEquals("XXXX XXXX 7733", kyc.maskedAccountNumber)
        assertEquals("PUNB0XXXXXX", kyc.maskedIfscCode)
        assertTrue(kyc.isKycApprovedForPayouts)
    }

    @Test
    fun testPayoutsStrictlyBlockedWithoutKyc() {
        val vm = IndiaShortsViewModel()
        vm.authenticateOwner("1234")
        assertTrue(vm.ownerWallet.value.isOwnerAuthenticated)

        val initialBalance = vm.ownerWallet.value.finalizedBalanceUsd

        // Step 1: Reset KYC to unverified state
        vm.resetOwnerKycForTesting()
        assertFalse(vm.ownerKyc.value.isKycApprovedForPayouts)
        assertFalse(vm.ownerWallet.value.isKycVerified)

        // Step 2: Attempt withdrawal - MUST BE BLOCKED
        vm.executeOwnerManualWithdrawal(1000.0)
        // Balance must remain unchanged!
        assertEquals(initialBalance, vm.ownerWallet.value.finalizedBalanceUsd, 0.001)

        // Step 3: Attempt scheduled 1st monthly payout - MUST BE BLOCKED
        vm.simulateScheduled1stMonthlyPayoutCycle()
        assertEquals(initialBalance, vm.ownerWallet.value.finalizedBalanceUsd, 0.001)

        // Step 4: Verify Mobile, Aadhaar & Bank Account (All 3 required)
        vm.submitOwnerMobileOtp("654321")
        vm.submitAadhaarKyc("123456787492", "984210")
        vm.submitAuthorizedBankAccountVerification("1234567890124018", "PUNB0123400", "KOKIL MURMU")
        assertTrue(vm.ownerKyc.value.isKycApprovedForPayouts)

        // Step 5: Now withdrawal should succeed
        vm.executeOwnerManualWithdrawal(1000.0)
        assertEquals(initialBalance - 1000.0, vm.ownerWallet.value.finalizedBalanceUsd, 0.001)
    }

    @Test
    fun testOwnerPrivacyWhenSwitchingToCreator() {
        val vm = IndiaShortsViewModel()
        vm.authenticateOwner("1234")
        assertTrue(vm.ownerWallet.value.isOwnerAuthenticated)

        // Switch to regular Creator
        vm.switchUser("creator_priya")
        val creator = vm.currentUser.value
        assertEquals("Priya Sharma", creator.name)
        assertFalse(creator.isOwner)

        // Owner private financial controls must be automatically locked
        assertFalse(vm.ownerWallet.value.isOwnerAuthenticated)
    }

    @Test
    fun testAuditTransactionsRecordedForKycAndBank() {
        val vm = IndiaShortsViewModel()
        vm.submitAadhaarKyc("123456787492", "984210")
        vm.submitPnbBankVerification("PUNB0123400", "4018")

        val txns = vm.transactions.value
        val kycTxn = txns.find { it.type == TransactionType.OWNER_KYC_VERIFICATION }
        assertNotNull("KYC transaction must be in audit ledger", kycTxn)
        assertEquals(TransactionStatus.SUCCESSFUL, kycTxn?.status)

        val bankTxn = txns.find { it.type == TransactionType.OWNER_BANK_VERIFICATION }
        assertNotNull("Bank verification transaction must be in audit ledger", bankTxn)
        assertEquals(TransactionStatus.SUCCESSFUL, bankTxn?.status)
    }

    @Test
    fun testOwnerKycStatusPendingApprovedRejected() {
        // Requirement 9: KYC Status: Pending, Approved, Rejected
        val statuses = OwnerKycStatus.values().map { it.label }
        assertTrue(statuses.contains("Pending"))
        assertTrue(statuses.contains("Approved"))
        assertTrue(statuses.contains("Rejected"))
    }

    @Test
    fun testAuthorizedKycProviderTwoStepOtpFlow() {
        val vm = IndiaShortsViewModel()

        // Step 1: Request OTP from Authorized Provider
        val reqSuccess = vm.requestProviderAadhaarOtp("123456787492")
        assertTrue(reqSuccess)
        val kycPending = vm.ownerKyc.value
        assertEquals(OwnerKycStatus.PENDING, kycPending.status)
        assertTrue(kycPending.isOtpRequested)
        assertNotNull(kycPending.providerSessionId)
        // Plaintext must NEVER be stored
        assertFalse(kycPending.aadhaarHashSha256.contains("123456787492"))
        assertEquals("XXXX XXXX 7492", kycPending.maskedAadhaar)

        // Step 2: Submit OTP
        val verifySuccess = vm.submitProviderAadhaarOtp("984210")
        assertTrue(verifySuccess)
        val kycApproved = vm.ownerKyc.value
        assertEquals(OwnerKycStatus.APPROVED, kycApproved.status)
        assertFalse(kycApproved.isOtpRequested)
    }

    @Test
    fun testAdminApproveAndRejectControls() {
        val vm = IndiaShortsViewModel()

        // Admin can reject KYC
        vm.adminRejectOwnerKyc("Document signature blurred")
        val kycRejected = vm.ownerKyc.value
        assertEquals(OwnerKycStatus.REJECTED, kycRejected.status)
        assertFalse(kycRejected.isKycApprovedForPayouts)

        // While rejected, payouts must be blocked
        vm.authenticateOwner("1234")
        val initialBalance = vm.ownerWallet.value.finalizedBalanceUsd
        vm.executeOwnerManualWithdrawal(500.0)
        assertEquals(initialBalance, vm.ownerWallet.value.finalizedBalanceUsd, 0.001)

        // Admin can approve KYC
        vm.adminApproveOwnerKyc("Cleared on manual audit")
        val kycApproved = vm.ownerKyc.value
        assertEquals(OwnerKycStatus.APPROVED, kycApproved.status)
        assertTrue(kycApproved.isKycApprovedForPayouts)
    }

    @Test
    fun testOnlyOwnerCanAccessKycSection() {
        val vm = IndiaShortsViewModel()

        // When Owner (Kokil Murmu) is active
        vm.switchUser("owner_kokil")
        vm.navigateTo(AppScreen.OWNER_KYC)
        assertEquals(AppScreen.OWNER_KYC, vm.currentScreen.value)

        // When Creator (Priya Sharma) is active, navigation to OWNER_KYC must be BLOCKED
        vm.switchUser("creator_priya")
        vm.navigateTo(AppScreen.SHORTS)
        assertEquals(AppScreen.SHORTS, vm.currentScreen.value)

        vm.navigateTo(AppScreen.OWNER_KYC)
        // Must NOT navigate to OWNER_KYC! Remains on SHORTS
        assertEquals(AppScreen.SHORTS, vm.currentScreen.value)
    }

    @Test
    fun testMobileNumberVerificationWithAuthorizedSmsGateway() {
        val vm = IndiaShortsViewModel()

        // Reset to pending
        vm.resetOwnerKycForTesting()
        assertEquals(MobileVerificationStatus.PENDING, vm.ownerKyc.value.mobileStatus)

        // Step 1: Request Mobile OTP
        val reqSuccess = vm.requestOwnerMobileOtp("9876543210")
        assertTrue(reqSuccess)
        val kycReq = vm.ownerKyc.value
        assertTrue(kycReq.isMobileOtpRequested)
        assertEquals("+91 ••••• •3210", kycReq.maskedMobileNumber)
        assertNotNull(kycReq.mobileProviderSessionId)

        // Step 2: Submit 6-digit OTP
        val verifySuccess = vm.submitOwnerMobileOtp("654321")
        assertTrue(verifySuccess)
        val kycVerified = vm.ownerKyc.value
        assertEquals(MobileVerificationStatus.VERIFIED, kycVerified.mobileStatus)
        assertFalse(kycVerified.isMobileOtpRequested)
    }

    @Test
    fun testBankAccountVerificationWithAuthorizedProvider() {
        val vm = IndiaShortsViewModel()

        val verified = vm.submitAuthorizedBankAccountVerification(
            accountNumberInput = "1234567890124018",
            ifscInput = "PUNB0123400",
            holderNameInput = "KOKIL MURMU"
        )
        assertTrue(verified)

        val kyc = vm.ownerKyc.value
        assertEquals(BankVerificationStatus.VERIFIED, kyc.bankVerificationStatus)
        assertEquals("XXXX XXXX 4018", kyc.maskedAccountNumber)
        assertEquals("KOKIL MURMU", kyc.bankAccountHolderVerifiedName)
        assertTrue(kyc.rawAccountNumberSavedOnServer)
    }

    @Test
    fun testTriVerificationWithdrawalGating() {
        val vm = IndiaShortsViewModel()
        vm.authenticateOwner("1234")

        // 1. If any of the 3 (Mobile, Aadhaar, Bank) is Pending, withdrawal MUST be blocked
        vm.resetOwnerKycForTesting()
        val initialBal = vm.ownerWallet.value.finalizedBalanceUsd

        vm.executeOwnerManualWithdrawal(100.0)
        assertEquals(initialBal, vm.ownerWallet.value.finalizedBalanceUsd, 0.001)

        // Verify only mobile
        vm.submitOwnerMobileOtp("654321")
        vm.executeOwnerManualWithdrawal(100.0)
        assertEquals(initialBal, vm.ownerWallet.value.finalizedBalanceUsd, 0.001)

        // Verify Aadhaar as well
        vm.submitAadhaarKyc("123456787492", "984210")
        vm.executeOwnerManualWithdrawal(100.0)
        assertEquals(initialBal, vm.ownerWallet.value.finalizedBalanceUsd, 0.001)

        // Finally verify Bank account - all 3 now verified
        vm.submitAuthorizedBankAccountVerification("1234567890124018", "PUNB0123400", "KOKIL MURMU")
        assertTrue(vm.ownerKyc.value.isKycApprovedForPayouts)

        // Now withdrawal MUST succeed
        vm.executeOwnerManualWithdrawal(100.0)
        assertEquals(initialBal - 100.0, vm.ownerWallet.value.finalizedBalanceUsd, 0.001)
    }

    // -------------------------------------------------------------
    // CREATOR JOIN MEMBERSHIP UNIT TESTS (Requirements 1 - 7)
    // -------------------------------------------------------------

    @Test
    fun testCreatorJoinEligibilityRule() {
        val vm = IndiaShortsViewModel()

        // Birsa Soren is MONETIZED -> JOIN button is eligible
        assertTrue(vm.isCreatorJoinEligible("@birsavillagevlogs"))

        // Priya Sharma is IN_PROGRESS (Non-monetized) -> JOIN button must NOT be eligible
        assertFalse(vm.isCreatorJoinEligible("@priyacomedy_hindi"))
    }

    @Test
    fun testSubscribeToCreatorMembershipWithRevenueSplit() {
        val vm = IndiaShortsViewModel()
        val initialCreatorEarnings = vm.creatorMembership.value.creatorNetMembershipEarningsInr
        val initialOwnerRevenue = vm.ownerMembershipOversight.value.ownerTotalMembershipRevenueInr
        val initialTotalMembers = vm.creatorMembership.value.totalMembers

        val goldPlan = vm.creatorMembership.value.plans.first { it.level == MembershipLevel.GOLD } // ₹199
        assertEquals(199.0, goldPlan.monthlyPriceInr, 0.001)

        val success = vm.subscribeToCreatorMembership(goldPlan, "Test Subscriber")
        assertTrue(success)

        // Member count incremented
        assertEquals(initialTotalMembers + 1, vm.creatorMembership.value.totalMembers)

        // User badge active
        assertTrue(vm.currentUser.value.isSubscribedToCreator)
        assertEquals(MembershipLevel.GOLD, vm.currentUser.value.activeMemberTier)

        // Revenue Split Check: 80% to Creator (₹159.20), 20% to Platform Owner (₹39.80)
        val expectedCreatorShare = 199.0 * 0.80
        val expectedOwnerShare = 199.0 * 0.20
        assertEquals(initialCreatorEarnings + expectedCreatorShare, vm.creatorMembership.value.creatorNetMembershipEarningsInr, 0.001)
        assertEquals(initialOwnerRevenue + expectedOwnerShare, vm.ownerMembershipOversight.value.ownerTotalMembershipRevenueInr, 0.001)
    }

    @Test
    fun testCancelMembershipSubscription() {
        val vm = IndiaShortsViewModel()
        val goldPlan = vm.creatorMembership.value.plans.first { it.level == MembershipLevel.GOLD }
        vm.subscribeToCreatorMembership(goldPlan, "Test Subscriber")
        assertTrue(vm.currentUser.value.isSubscribedToCreator)

        val activeMembersBefore = vm.creatorMembership.value.activeMembers
        vm.cancelMemberSubscription()

        // Subscription revoked
        assertFalse(vm.currentUser.value.isSubscribedToCreator)
        assertNull(vm.currentUser.value.activeMemberTier)
        assertEquals(activeMembersBefore - 1, vm.creatorMembership.value.activeMembers)
    }

    @Test
    fun testOwnerMembershipCommissionAndRefunds() {
        val vm = IndiaShortsViewModel()

        // Test Commission Adjustment (e.g. 25% owner, 75% creator)
        vm.updateMembershipCommission(25.0)
        assertEquals(25.0, vm.ownerMembershipOversight.value.ownerPlatformCommissionPercent, 0.001)
        assertEquals(75.0, vm.ownerMembershipOversight.value.creatorRevenuePercent, 0.001)

        // Test Dispute & Refund Processing
        val initialRefunds = vm.ownerMembershipOversight.value.totalRefundsProcessedInr
        vm.processMemberRefund(199.0, "Test Refund")
        assertEquals(initialRefunds + 199.0, vm.ownerMembershipOversight.value.totalRefundsProcessedInr, 0.001)
    }
}
