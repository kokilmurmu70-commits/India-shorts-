package com.example.model

enum class VideoType {
    SHORT, LONG
}

enum class VideoCategory(val displayName: String) {
    COMEDY("Comedy"),
    VILLAGE_VLOG("Village Vlog"),
    EDUCATIONAL("Educational"),
    CULTURAL("Cultural"),
    ENTERTAINMENT("Entertainment"),
    MUSIC("Music"),
    TRAVEL("Travel"),
    ORIGINAL("Original")
}

enum class MonetizationStatus(val label: String) {
    NOT_ELIGIBLE("Not Eligible"),
    IN_PROGRESS("In Progress"),
    ELIGIBLE("Eligible"),
    MONETIZED("Monetized"),
    SUSPENDED("Suspended")
}

enum class TransactionType {
    CREATOR_WITHDRAWAL,
    OWNER_MONTHLY_PAYOUT,
    OWNER_MANUAL_WITHDRAWAL,
    AD_REVENUE_SETTLEMENT,
    SPECIAL_REWARD_PAYOUT,
    OWNER_KYC_VERIFICATION,
    OWNER_BANK_VERIFICATION,
    OWNER_MOBILE_VERIFICATION,
    MEMBERSHIP_SUBSCRIPTION,
    MEMBERSHIP_REFUND
}

enum class OwnerKycStatus(val label: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected")
}

enum class KycStatus(val label: String) {
    NOT_SUBMITTED("Not Submitted"),
    IN_REVIEW("Under Review"),
    VERIFIED("Verified & Active"),
    REJECTED("Verification Failed")
}

enum class BankVerificationStatus(val label: String) {
    UNVERIFIED("Unverified"),
    PENNY_DROP_PENDING("Penny Drop Verification Pending"),
    VERIFIED("Verified by Penny Drop"),
    FAILED("Verification Failed")
}

enum class MobileVerificationStatus(val label: String) {
    PENDING("Pending"),
    VERIFIED("Verified")
}

data class OwnerKycState(
    val status: OwnerKycStatus = OwnerKycStatus.APPROVED,
    // 1. Mobile Number Verification (Authorized SMS Provider Gateway)
    val mobileStatus: MobileVerificationStatus = MobileVerificationStatus.VERIFIED,
    val ownerMobileNumber: String = "+91 98765 43210",
    val maskedMobileNumber: String = "+91 ••••• •3210",
    val isMobileOtpRequested: Boolean = false,
    val mobileProviderSessionId: String? = "SMS-GATEWAY-20261015-9921",
    val mobileVerifiedDate: String = "15 Oct 2026",
    val authorizedSmsProviderName: String = "Govt/TRAI Authorized DLT SMS Gateway (Fast2SMS/Gupshup)",

    // 2. Aadhaar Verification (Authorized UIDAI ASA Gateway)
    val aadhaarStatus: KycStatus = KycStatus.VERIFIED,
    val maskedAadhaar: String = "XXXX XXXX 7492",
    val aadhaarHashSha256: String = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", // Salted SHA-256, NEVER plaintext
    val verifiedDate: String = "15 Oct 2026",
    val authorizedProviderName: String = "UIDAI Authorized e-KYC Vault Gateway (ASA/KUA)",
    val providerSessionId: String? = "UIDAI-AUTH-20261015-882194",
    val isOtpRequested: Boolean = false,
    val isBackendConnected: Boolean = true,
    val backendEndpointUrl: String = "https://api.indiashorts.internal/v1/owner-kyc/uidai",
    val lastBackendError: String? = null,
    val fullNameAsPerAadhaar: String = "Kokil Murmu",
    val maskedDob: String = "XX-XX-1996",
    val gender: String = "Male",

    // 3. Bank Account Verification (Authorized Banking Gateway Penny Drop)
    val bankVerificationStatus: BankVerificationStatus = BankVerificationStatus.VERIFIED,
    val designatedBank: String = "PUNJAB NATIONAL BANK (PNB)",
    val maskedAccountNumber: String = "XXXX XXXX 4018",
    val rawAccountNumberSavedOnServer: Boolean = true, // Raw details stored on backend server only
    val maskedIfscCode: String = "PUNB0XXXXXX",
    val beneficiaryName: String = "KOKIL MURMU",
    val bankAccountHolderVerifiedName: String = "KOKIL MURMU",
    val pennyDropRefId: String = "PENNY-PNB-20261015-8821",
    val pennyDropAmountFormatted: String = "₹1.00 (Credited & Verified)",
    val authorizedBankingGatewayName: String = "NPCI / RBI Authorized e-Mandate & Penny-Drop API Gateway",

    // Tri-Verification Clearance & Admin Controls
    val isKycApprovedForPayouts: Boolean = true,
    val adminReviewNote: String = "Approved by Kokil Murmu (Master Admin) following Mobile SMS, UIDAI ASA provider clearance & PNB Penny Drop match.",
    val adminReviewedBy: String = "Master Admin (Kokil Murmu)",
    val encryptionStandard: String = "AES-256-GCM + Salted SHA-256 Hashing",
    val lastAuditTimestamp: String = "2026-10-15 11:30:00 IST",
    val zeroExposureNotice: String = "Exclusively reserved for India Shorts App Owner. Creators do not require KYC and cannot access Owner records."
)

enum class TransactionStatus {
    PENDING,
    PROCESSING,
    SUCCESSFUL,
    FAILED,
    RETRYING
}

enum class ReportCategory(val title: String) {
    ADULT_CONTENT("Adult / Explicit Content"),
    MINOR_SAFETY("Protection of Minors Violation"),
    COPYRIGHT("Copyright Infringement"),
    HARASSMENT("Harassment & Bullying"),
    SPAM("Spam & Scams"),
    DANGEROUS("Dangerous Activities"),
    FAKE_INFO("Fake Information")
}

enum class NotificationType {
    FOLLOWER,
    LIKE,
    COMMENT,
    MONETIZATION,
    WALLET,
    COPYRIGHT,
    SYSTEM,
    REWARD
}

enum class MembershipLevel(val displayName: String, val badgeColorHex: Long) {
    SILVER("Silver Fan", 0xFF94A3B8),
    GOLD("Gold Member", 0xFFF59E0B),
    DIAMOND("Diamond VIP", 0xFF06B6D4)
}

data class MembershipPlan(
    val id: String,
    val creatorId: String,
    val creatorName: String,
    val level: MembershipLevel,
    val monthlyPriceInr: Double,
    val perks: List<String>,
    val badgeName: String,
    val isActive: Boolean = true
)

data class MemberSubscription(
    val id: String,
    val subscriberUserId: String,
    val subscriberName: String,
    val creatorId: String,
    val creatorName: String,
    val planId: String,
    val level: MembershipLevel,
    val monthlyFeeInr: Double,
    val startDate: String,
    val nextRenewalDate: String,
    val gatewayPaymentRef: String,
    val isAutoRenewActive: Boolean = true,
    val status: String = "ACTIVE" // ACTIVE, CANCELLED, REFUNDED
)

data class CreatorMembershipState(
    val creatorId: String = "creator_birsa",
    val isMonetizationApproved: Boolean = true,
    val totalMembers: Int = 184,
    val activeMembers: Int = 176,
    val monthlyGrossMembershipInr: Double = 34960.0,
    val creatorNetMembershipEarningsInr: Double = 27968.0, // 80% to Creator, 20% to Platform Owner
    val plans: List<MembershipPlan> = listOf(
        MembershipPlan(
            id = "plan_silver",
            creatorId = "creator_birsa",
            creatorName = "Birsa Soren",
            level = MembershipLevel.SILVER,
            monthlyPriceInr = 59.0,
            perks = listOf("Exclusive Member Badge in Comments", "Members-Only Community Posts", "Priority Comment Replies"),
            badgeName = "Silver Badge"
        ),
        MembershipPlan(
            id = "plan_gold",
            creatorId = "creator_birsa",
            creatorName = "Birsa Soren",
            level = MembershipLevel.GOLD,
            monthlyPriceInr = 199.0,
            perks = listOf("All Silver Perks", "Exclusive Behind-the-Scenes Videos", "24-Hour Early Video Access", "Members-Only Live Streams"),
            badgeName = "Gold VIP Badge"
        ),
        MembershipPlan(
            id = "plan_diamond",
            creatorId = "creator_birsa",
            creatorName = "Birsa Soren",
            level = MembershipLevel.DIAMOND,
            monthlyPriceInr = 499.0,
            perks = listOf("All Gold Perks", "Monthly 1-on-1 Q&A Call", "Name in Video End-Credits", "Exclusive Physical Souvenir (Annual)"),
            badgeName = "Diamond Elite Badge"
        )
    ),
    val recentMembers: List<MemberSubscription> = emptyList()
)

data class OwnerMembershipOversightState(
    val platformTotalMembers: Int = 4280,
    val platformGrossMembershipRevenueInr: Double = 684800.0,
    val ownerPlatformCommissionPercent: Double = 20.0, // 20% Platform Owner Commission
    val creatorRevenuePercent: Double = 80.0, // 80% Creator Revenue
    val ownerTotalMembershipRevenueInr: Double = 136960.0,
    val totalActiveCreatorsWithMembership: Int = 86,
    val totalRefundsProcessedInr: Double = 2360.0,
    val pendingDisputesCount: Int = 2
)

data class UserProfile(
    val id: String,
    val name: String,
    val username: String,
    val mobileNumber: String,
    val bio: String,
    val avatarColorHex: Long = 0xFFFF6D00,
    val isCreator: Boolean = true,
    val isOwner: Boolean = false, // Kokil Murmu
    val followersCount: Int = 12400,
    val followingCount: Int = 180,
    val verifiedBadge: Boolean = true,
    val monetizationStatus: MonetizationStatus = MonetizationStatus.MONETIZED,
    val activeMemberTier: MembershipLevel? = null,
    val isSubscribedToCreator: Boolean = false
)

data class VideoItem(
    val id: String,
    val title: String,
    val description: String,
    val creatorName: String,
    val creatorUsername: String,
    val creatorAvatarColor: Long,
    val videoType: VideoType,
    val category: VideoCategory,
    val language: String,
    val durationText: String,
    val viewsCount: Long,
    val eligibleViewsCount: Long,
    val likesCount: Long,
    val commentsCount: Long,
    val sharesCount: Long,
    val soundTitle: String,
    val soundArtist: String,
    val hashtags: List<String>,
    val gradientStartHex: Long,
    val gradientEndHex: Long,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isFollowingCreator: Boolean = false,
    val copyrightCertified: Boolean = true,
    val captionsText: String = "",
    val isMembersOnly: Boolean = false,
    val requiredMembershipTier: MembershipLevel = MembershipLevel.SILVER
)

data class CommentItem(
    val id: String,
    val videoId: String,
    val authorName: String,
    val content: String,
    val timestamp: String,
    val likesCount: Int,
    val isCreatorPinned: Boolean = false,
    val creatorReply: String? = null
)

data class SongItem(
    val id: String,
    val title: String,
    val artist: String,
    val language: String,
    val duration: String,
    val usageCount: String,
    val isLicensed: Boolean = true,
    val licenseBadge: String = "Certified Authorized Library"
)

data class MonetizationProgress(
    val followers: Int = 1420,
    val followerTarget: Int = 500,
    val watchHours: Int = 3450,
    val watchHoursTarget: Int = 2000,
    val shortsViews: Long = 185000L,
    val shortsViewsTarget: Long = 100000L,
    val status: MonetizationStatus = MonetizationStatus.MONETIZED,
    val totalEarningsUsd: Double = 348.60,
    val availableBalanceUsd: Double = 280.00,
    val pendingBalanceUsd: Double = 68.60,
    val minimumWithdrawalUsd: Double = 200.00,
    val originalContentCompliant: Boolean = true,
    val copyrightStrikes: Int = 0,
    // 40% Creator Pool metrics
    val creatorTotalViews: Long = 120_000L,
    val creatorEligibleViews: Long = 100_000L,
    val allCreatorsEligibleViews: Long = 1_000_000L,
    val estimatedEarningsInr: Double = 400.0,
    val verifiedEarningsInr: Double = 400.0,
    val pendingEarningsInr: Double = 0.0,
    val approvedEarningsInr: Double = 400.0,
    val withdrawableBalanceInr: Double = 400.0,
    val monthlyEarningsInr: Double = 400.0
)

data class AdMobConfig(
    val isTestMode: Boolean = true,
    val appId: String = "ca-app-pub-3940256099942544~3347511713", // Official Google Test App ID
    val bannerAdUnitId: String = "ca-app-pub-3940256099942544/6300978111", // Google Test Banner
    val interstitialAdUnitId: String = "ca-app-pub-3940256099942544/1033173712", // Google Test Interstitial
    val nativeAdUnitId: String = "ca-app-pub-3940256099942544/2247696110", // Google Test Native
    val isConfiguredInBackend: Boolean = false,
    val antiBotClickProtectionEnabled: Boolean = true,
    val invalidTrafficFilterStatus: String = "Active (Filtering Bot Impressions & Self-Clicks)"
)

data class AdItem(
    val id: String,
    val title: String,
    val sponsorName: String,
    val description: String,
    val callToAction: String,
    val targetUrl: String,
    val adType: String, // NATIVE_SHORTS, BANNER_HOME, VIDEO_PREROLL, SPONSORED_POST
    val isTestAd: Boolean = true,
    val verifiedCpmInr: Double = 40.0
)

data class OwnerWalletState(
    val ownerName: String = "KOKIL MURMU",
    val bankName: String = "PUNJAB NATIONAL BANK (PNB)",
    val isDemoMode: Boolean = true,
    val isOwnerAuthenticated: Boolean = false,
    val backendStorageNote: String = "Bank details stored securely on backend server only",
    val gatewayStatus: String = "Awaiting Live Payment Gateway Integration",
    // 60% Owner Share vs 40% Creator Pool (MANDATORY RULE)
    val totalAppAdsRevenueInr: Double = 10_000.0,
    val totalVerifiedRevenueInr: Double = 10_000.0,
    val creatorPoolPercentage: Double = 40.0,
    val ownerSharePercentage: Double = 60.0,
    val totalCreatorPoolInr: Double = 4_000.0,
    val ownerShareInr: Double = 6_000.0,
    val totalRegisteredCreators: Int = 142,
    val totalPlatformEligibleViews: Long = 1_000_000L,
    val grossPlatformRevenueUsd: Double = 54200.00,
    val ownerTotalEarningsUsd: Double = 46973.35,
    val availableBalanceUsd: Double = 14850.00,
    val pendingBalanceUsd: Double = 3200.00,
    val finalizedBalanceUsd: Double = 14850.00,
    val automaticMonthlyPayoutEnabled: Boolean = true,
    val nextScheduledPayoutDate: String = "1st of Coming Month (09:00 AM IST)",
    val payoutProviderName: String = "Authorized Banking API Gateway (Backend Vault)",
    val isKycVerified: Boolean = true,
    val isBankVerified: Boolean = true,
    val kycStatusLabel: String = "Aadhaar e-KYC Verified",
    val ownerKycStatus: OwnerKycStatus = OwnerKycStatus.APPROVED,
    val maskedAadhaar: String = "XXXX XXXX 7492"
)

data class TransactionRecord(
    val id: String,
    val txnId: String,
    val type: TransactionType,
    val amountFormatted: String,
    val destinationAccount: String,
    val timestamp: String,
    val status: TransactionStatus,
    val note: String
)

data class SpecialRewardState(
    val targetViews: Long = 1_100_000_000L, // 1.10 Billion
    val currentEligibleViews: Long = 428_950_000L,
    val rewardAmountInr: Long = 1_000_000L, // ₹10,00,000 (10 Lakh)
    val isApprovedAndFunded: Boolean = true,
    val claimStatus: String = "In Progress (39.0% achieved)"
)

data class CopyrightScanResult(
    val videoId: String,
    val videoTitle: String,
    val matchPercentage: Double,
    val detectedAudio: String,
    val isPassed: Boolean,
    val message: String
)

data class ViolationReportItem(
    val id: String,
    val targetContent: String,
    val category: ReportCategory,
    val status: String,
    val timestamp: String,
    val resolutionNote: String
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timeAgo: String,
    var isRead: Boolean = false
)

data class SupportedLanguage(
    val code: String,
    val englishName: String,
    val nativeName: String,
    val region: String
)

data class AppControlItem(
    val id: Int, // 1..60
    val domain: String,
    val labelName: String,
    val buttonName: String,
    val actionId: String,
    val description: String
)
