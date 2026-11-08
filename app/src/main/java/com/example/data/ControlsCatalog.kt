package com.example.data

import com.example.model.AppControlItem

object ControlsCatalog {

    /**
     * EXACTLY 60 Labels and 60 Buttons as mandated by Requirement 4:
     * "Create exactly 60 meaningful Labels and 60 functional Buttons.
     * Every label must have a unique meaningful name.
     * Every button must have a unique working function.
     * Do not create duplicate, empty or useless controls.
     * Organize all controls across:
     * Home, Shorts, Videos, Upload, Search, Trending, Music, Likes, Comments,
     * Sharing, Followers, Creator Profile, Login, Registration, Notifications,
     * Language, Settings, Monetization, Creator Earnings, Owner Wallet,
     * Withdraw, Bank Account, Copyright, Reports, Admin Dashboard.
     * Keep the total exactly 60 Labels and 60 Buttons."
     */
    val ALL_60_CONTROLS: List<AppControlItem> = listOf(
        // 1. Home (2)
        AppControlItem(
            id = 1,
            domain = "Home",
            labelName = "Label 1: Home Feed Recommendation Matrix",
            buttonName = "Button 1: Refresh Home Feed",
            actionId = "ACTION_REFRESH_HOME",
            description = "Refreshes user personalized recommendation algorithmic stream"
        ),
        AppControlItem(
            id = 2,
            domain = "Home",
            labelName = "Label 2: Top Creator Spotlight Banner",
            buttonName = "Button 2: Explore Featured Channels",
            actionId = "ACTION_EXPLORE_CHANNELS",
            description = "Navigates to curated Indian regional & global creator spotlight"
        ),

        // 2. Shorts (3)
        AppControlItem(
            id = 3,
            domain = "Shorts",
            labelName = "Label 3: Vertical Shorts Video Playback State",
            buttonName = "Button 3: Toggle Shorts Play/Pause",
            actionId = "ACTION_TOGGLE_SHORTS_PLAY",
            description = "Controls real-time video playback pause and resume state"
        ),
        AppControlItem(
            id = 4,
            domain = "Shorts",
            labelName = "Label 4: Real-Time Shorts Engagement Counter",
            buttonName = "Button 4: Next Short Video",
            actionId = "ACTION_NEXT_SHORT",
            description = "Advances to the next trending short video in feed"
        ),
        AppControlItem(
            id = 5,
            domain = "Shorts",
            labelName = "Label 5: Certified Background Audio Track Indicator",
            buttonName = "Button 5: Bookmark to Watchlist",
            actionId = "ACTION_BOOKMARK_SHORT",
            description = "Saves current short video into offline-ready saved collection"
        ),

        // 3. Videos (Long-form) (3)
        AppControlItem(
            id = 6,
            domain = "Videos",
            labelName = "Label 6: High-Definition Resolution Selector (1080p/720p/Auto)",
            buttonName = "Button 6: Toggle Video Resolution",
            actionId = "ACTION_CHANGE_RESOLUTION",
            description = "Cycles streaming bitrate between 1080p, 720p, 480p and Auto"
        ),
        AppControlItem(
            id = 7,
            domain = "Videos",
            labelName = "Label 7: Real-Time Multilingual Closed Captions Stream",
            buttonName = "Button 7: Toggle Closed Captions (CC)",
            actionId = "ACTION_TOGGLE_CC",
            description = "Enables or disables on-screen synchronized subtitles"
        ),
        AppControlItem(
            id = 8,
            domain = "Videos",
            labelName = "Label 8: Cinema Theater & Fullscreen Display Engine",
            buttonName = "Button 8: Expand Fullscreen Player",
            actionId = "ACTION_TOGGLE_FULLSCREEN",
            description = "Switches video player into expanded immersive mode"
        ),

        // 4. Upload (3)
        AppControlItem(
            id = 9,
            domain = "Upload",
            labelName = "Label 9: Video File Pipeline & Hardware Encoding",
            buttonName = "Button 9: Choose Video Source File",
            actionId = "ACTION_CHOOSE_UPLOAD_FILE",
            description = "Selects media from storage for rapid compression & staging"
        ),
        AppControlItem(
            id = 10,
            domain = "Upload",
            labelName = "Label 10: AI Smart Video Thumbnail Generator",
            buttonName = "Button 10: Generate AI Video Cover",
            actionId = "ACTION_GENERATE_THUMBNAIL",
            description = "Analyzes keyframes and produces high CTR video poster"
        ),
        AppControlItem(
            id = 11,
            domain = "Upload",
            labelName = "Label 11: Global Category & Hashtag Taxonomy",
            buttonName = "Button 11: Publish Video to Network",
            actionId = "ACTION_PUBLISH_VIDEO",
            description = "Finalizes metadata, verifies content ID, and broadcasts video"
        ),

        // 5. Search (2)
        AppControlItem(
            id = 12,
            domain = "Search",
            labelName = "Label 12: Unified Multi-Modal Search Filter",
            buttonName = "Button 12: Execute Search Query",
            actionId = "ACTION_RUN_SEARCH",
            description = "Scans videos, channels, songs, and hashtags across all languages"
        ),
        AppControlItem(
            id = 13,
            domain = "Search",
            labelName = "Label 13: Viral Search Trends & Recent Queries",
            buttonName = "Button 13: Clear Search History Cache",
            actionId = "ACTION_CLEAR_SEARCH_HISTORY",
            description = "Wipes local search cache and refreshes hot query suggestions"
        ),

        // 6. Trending (3)
        AppControlItem(
            id = 14,
            domain = "Trending",
            labelName = "Label 14: Worldwide & Country-Wise Trending Radar",
            buttonName = "Button 14: Filter Trends by Country",
            actionId = "ACTION_FILTER_COUNTRY_TRENDS",
            description = "Switches viral rankings between India, Worldwide, and South Asia"
        ),
        AppControlItem(
            id = 15,
            domain = "Trending",
            labelName = "Label 15: Indian States Regional Viral Index",
            buttonName = "Button 15: Filter Trends by Indian State",
            actionId = "ACTION_FILTER_STATE_TRENDS",
            description = "Inspects viral shorts in Jharkhand, Bihar, Bengal, Maharashtra, etc."
        ),
        AppControlItem(
            id = 16,
            domain = "Trending",
            labelName = "Label 16: Fair Algorithmic Velocity Metric",
            buttonName = "Button 16: Re-calculate Fair Algorithm Trends",
            actionId = "ACTION_REFRESH_ALGORITHM",
            description = "Applies organic engagement calculation without artificial bias"
        ),

        // 7. Music (2)
        AppControlItem(
            id = 17,
            domain = "Music",
            labelName = "Label 17: Authorized Audio Library & Copyright Clearance",
            buttonName = "Button 17: Preview Soundtrack Audio",
            actionId = "ACTION_PREVIEW_MUSIC",
            description = "Plays high quality audio snippet with copyright license stamp"
        ),
        AppControlItem(
            id = 18,
            domain = "Music",
            labelName = "Label 18: Trending Regional Sounds Chart (Santali, Khortha, etc.)",
            buttonName = "Button 18: Use Audio in New Short",
            actionId = "ACTION_USE_AUDIO_IN_SHORT",
            description = "Attaches certified audio track to user's short creator studio"
        ),

        // 8. Likes (2)
        AppControlItem(
            id = 19,
            domain = "Likes",
            labelName = "Label 19: Real-Time Heart Counter & Appreciation Index",
            buttonName = "Button 19: Toggle Like Reaction",
            actionId = "ACTION_TOGGLE_LIKE",
            description = "Adds or removes user heart and increments public like count"
        ),
        AppControlItem(
            id = 20,
            domain = "Likes",
            labelName = "Label 20: User Liked Videos Personal Vault",
            buttonName = "Button 20: Open Liked Videos Collection",
            actionId = "ACTION_VIEW_LIKED_COLLECTION",
            description = "Displays chronological feed of user's liked videos"
        ),

        // 9. Comments (2)
        AppControlItem(
            id = 21,
            domain = "Comments",
            labelName = "Label 21: Public Community Discussion & Moderation Queue",
            buttonName = "Button 21: Post Comment / Reply",
            actionId = "ACTION_POST_COMMENT",
            description = "Submits user text response to public video discussion"
        ),
        AppControlItem(
            id = 22,
            domain = "Comments",
            labelName = "Label 22: Official Creator Pinned Reply Badge",
            buttonName = "Button 22: Send Quick Emoji Reaction",
            actionId = "ACTION_SEND_QUICK_EMOJI",
            description = "Dispatches instant appreciation emoji into comment section"
        ),

        // 10. Sharing (2)
        AppControlItem(
            id = 23,
            domain = "Sharing",
            labelName = "Label 23: Direct Deep-Link & QR Code Generator",
            buttonName = "Button 23: Copy Video Direct Link",
            actionId = "ACTION_COPY_SHARE_LINK",
            description = "Copies universal web and mobile link to device clipboard"
        ),
        AppControlItem(
            id = 24,
            domain = "Sharing",
            labelName = "Label 24: Cross-Platform Social Broadcast Destination",
            buttonName = "Button 24: Share to WhatsApp & External Apps",
            actionId = "ACTION_SHARE_EXTERNAL",
            description = "Launches system sharing sheet for WhatsApp, X, Facebook, and Email"
        ),

        // 11. Followers (2)
        AppControlItem(
            id = 25,
            domain = "Followers",
            labelName = "Label 25: Creator Community & Subscriber Count",
            buttonName = "Button 25: Toggle Follow Creator",
            actionId = "ACTION_TOGGLE_FOLLOW",
            description = "Subscribes to creator updates and adds channel to feed"
        ),
        AppControlItem(
            id = 26,
            domain = "Followers",
            labelName = "Label 26: Creator Notification Bell & Priority Alerts",
            buttonName = "Button 26: Toggle Channel Notification Bell",
            actionId = "ACTION_TOGGLE_BELL",
            description = "Enables push notifications for new uploads from this creator"
        ),

        // 12. Creator Profile (3)
        AppControlItem(
            id = 27,
            domain = "Creator Profile",
            labelName = "Label 27: Official Creator Channel Verification & Biography",
            buttonName = "Button 27: Edit Channel Profile & Bio",
            actionId = "ACTION_EDIT_PROFILE",
            description = "Updates channel description, banner, and display credentials"
        ),
        AppControlItem(
            id = 28,
            domain = "Creator Profile",
            labelName = "Label 28: Total Channel Lifetime Cumulative Views",
            buttonName = "Button 28: Toggle Creator / Viewer Studio Mode",
            actionId = "ACTION_SWITCH_STUDIO_MODE",
            description = "Switches view between public consumer mode and creator management"
        ),
        AppControlItem(
            id = 29,
            domain = "Creator Profile",
            labelName = "Label 29: Creator Shield & Community Reputation Tier",
            buttonName = "Button 29: Share Channel Profile Link",
            actionId = "ACTION_SHARE_CHANNEL",
            description = "Generates dedicated channel URL for external social promotion"
        ),

        // 13. Login (2)
        AppControlItem(
            id = 30,
            domain = "Login",
            labelName = "Label 30: Encrypted Mobile Number Login Gateway",
            buttonName = "Button 30: Request Secure Mobile OTP",
            actionId = "ACTION_REQUEST_OTP",
            description = "Dispatches SMS one-time security code to registered phone"
        ),
        AppControlItem(
            id = 31,
            domain = "Login",
            labelName = "Label 31: Zero-Trust OTP Authentication Protocol",
            buttonName = "Button 31: Verify OTP and Login",
            actionId = "ACTION_VERIFY_OTP_LOGIN",
            description = "Authenticates cryptographic OTP code and starts session"
        ),

        // 14. Registration (2)
        AppControlItem(
            id = 32,
            domain = "Registration",
            labelName = "Label 32: Global Account Registration & User Onboarding",
            buttonName = "Button 32: Submit New User Registration",
            actionId = "ACTION_SUBMIT_REGISTRATION",
            description = "Creates verified viewer/creator account on INDIA SHORTS"
        ),
        AppControlItem(
            id = 33,
            domain = "Registration",
            labelName = "Label 33: Account Recovery & Two-Factor Vault",
            buttonName = "Button 33: Initiate Account Recovery",
            actionId = "ACTION_ACCOUNT_RECOVERY",
            description = "Restores credentials via verified backup phone or email"
        ),

        // 15. Notifications (2)
        AppControlItem(
            id = 34,
            domain = "Notifications",
            labelName = "Label 34: Instant Push Activity Feed & Mentions",
            buttonName = "Button 34: Mark All Notifications Read",
            actionId = "ACTION_MARK_NOTIFS_READ",
            description = "Clears unread alert badges across all activity streams"
        ),
        AppControlItem(
            id = 35,
            domain = "Notifications",
            labelName = "Label 35: Financial & Monetization Alert Ledger",
            buttonName = "Button 35: Clear All Alert History",
            actionId = "ACTION_CLEAR_ALL_NOTIFS",
            description = "Archives completed system and financial transfer notices"
        ),

        // 16. Language (2)
        AppControlItem(
            id = 36,
            domain = "Language",
            labelName = "Label 36: Multilingual Engine (20 Indian & Global Languages)",
            buttonName = "Button 36: Apply Selected Language",
            actionId = "ACTION_APPLY_LANGUAGE",
            description = "Switches user interface and recommendations to target dialect"
        ),
        AppControlItem(
            id = 37,
            domain = "Language",
            labelName = "Label 37: Regional Language Subtitle Auto-Translation",
            buttonName = "Button 37: Toggle Multilingual Subtitles",
            actionId = "ACTION_TOGGLE_MULTI_SUBTITLES",
            description = "Enables automatic localized captions (Santali, Hindi, Tamil, etc.)"
        ),

        // 17. Settings (2)
        AppControlItem(
            id = 38,
            domain = "Settings",
            labelName = "Label 38: System Theme & Display Engine (Dark/Light)",
            buttonName = "Button 38: Toggle Dark / Light Theme",
            actionId = "ACTION_TOGGLE_THEME",
            description = "Alternates visual theme between high contrast dark and crisp light"
        ),
        AppControlItem(
            id = 39,
            domain = "Settings",
            labelName = "Label 39: GDPR & Digital Data Privacy Settings",
            buttonName = "Button 39: Manage Data Privacy & Account Deletion",
            actionId = "ACTION_MANAGE_PRIVACY",
            description = "Opens exportable data vault and voluntary account removal"
        ),

        // 18. Monetization (3)
        AppControlItem(
            id = 40,
            domain = "Monetization",
            labelName = "Label 40: Creator Monetization Eligibility Progress Tracker",
            buttonName = "Button 40: Submit Partner Monetization Application",
            actionId = "ACTION_SUBMIT_MONETIZATION_APP",
            description = "Audits channel metrics against 500 followers and 2,000 hrs / 100k views"
        ),
        AppControlItem(
            id = 41,
            domain = "Monetization",
            labelName = "Label 41: Public Watch Hours & Shorts Views Compliance Gauge",
            buttonName = "Button 41: Check Real-Time Policy Compliance",
            actionId = "ACTION_CHECK_COMPLIANCE",
            description = "Scans uploads for community guidelines and copyright standing"
        ),
        AppControlItem(
            id = 42,
            domain = "Monetization",
            labelName = "Label 42: Transparent Revenue Sharing Model (13.33% / 86.67%)",
            buttonName = "Button 42: View Official Revenue Split Formula",
            actionId = "ACTION_VIEW_REVENUE_FORMULA",
            description = "Explains $8 Creator / $52 Owner per $60 example ad revenue breakdown"
        ),

        // 19. Creator Earnings (2)
        AppControlItem(
            id = 43,
            domain = "Creator Earnings",
            labelName = "Label 43: Creator Available & Finalized Revenue Balance",
            buttonName = "Button 43: Calculate Verified View Earnings",
            actionId = "ACTION_CALCULATE_VIEW_EARNINGS",
            description = "Applies 13.3333% share on verified anti-bot eligible impressions"
        ),
        AppControlItem(
            id = 44,
            domain = "Creator Earnings",
            labelName = "Label 44: Eligible Monetized Views Ledger & Breakdown",
            buttonName = "Button 44: Download Creator Earnings Statement",
            actionId = "ACTION_DOWNLOAD_EARNINGS_STATEMENT",
            description = "Generates audit statement of verified views and ad revenues"
        ),

        // 20. Owner Wallet (Kokil Murmu) (3)
        AppControlItem(
            id = 45,
            domain = "Owner Wallet",
            labelName = "Label 45: Official Owner Wallet & Ledger (Kokil Murmu)",
            buttonName = "Button 45: Authenticate Owner Private Vault Access",
            actionId = "ACTION_ACCESS_OWNER_VAULT",
            description = "Confirms Kokil Murmu owner credentials to access financial controls"
        ),
        AppControlItem(
            id = 46,
            domain = "Owner Wallet",
            labelName = "Label 46: Platform Ad Revenue Share Ledger (86.6667%)",
            buttonName = "Button 46: Generate Owner Monthly Statement",
            actionId = "ACTION_GEN_OWNER_MONTHLY_STATEMENT",
            description = "Generates detailed balance sheet of platform gross and owner net"
        ),
        AppControlItem(
            id = 47,
            domain = "Owner Wallet",
            labelName = "Label 47: Punjab National Bank Monthly Automatic Payout Schedule",
            buttonName = "Button 47: Toggle Automatic Monthly Payout",
            actionId = "ACTION_TOGGLE_AUTO_PAYOUT",
            description = "Enables or disables scheduled 1st-of-month bank wire to PNB"
        ),

        // 21. Withdraw (Creator & Owner) (3)
        AppControlItem(
            id = 48,
            domain = "Withdraw",
            labelName = "Label 48: Creator Withdrawal Terminal ($200 Minimum Limit)",
            buttonName = "Button 48: Request Creator Bank Withdrawal ($200 Min)",
            actionId = "ACTION_SUBMIT_CREATOR_WITHDRAWAL",
            description = "Initiates bank transfer when creator balance reaches $200"
        ),
        AppControlItem(
            id = 49,
            domain = "Withdraw",
            labelName = "Label 49: Owner Direct Manual Withdrawal Engine",
            buttonName = "Button 49: Execute Owner Instant Manual Withdrawal",
            actionId = "ACTION_EXECUTE_OWNER_WITHDRAWAL",
            description = "Dispatches verified owner funds to Punjab National Bank account"
        ),
        AppControlItem(
            id = 50,
            domain = "Withdraw",
            labelName = "Label 50: Banking Transaction Status & Retry Pipeline",
            buttonName = "Button 50: Retry Failed Transfer Safely",
            actionId = "ACTION_RETRY_FAILED_TRANSFER",
            description = "Re-initiates payment gateway verification for failed transactions"
        ),

        // 22. Bank Account (Punjab National Bank) (2)
        AppControlItem(
            id = 51,
            domain = "Bank Account",
            labelName = "Label 51: Owner Punjab National Bank (PNB) Account Verification",
            buttonName = "Button 51: Verify PNB Bank Account & IFSC",
            actionId = "ACTION_VERIFY_PNB_BANK",
            description = "Runs electronic penny drop verification for owner PNB account"
        ),
        AppControlItem(
            id = 52,
            domain = "Bank Account",
            labelName = "Label 52: Secure Payout Banking Details & Encryption",
            buttonName = "Button 52: Update Secure Payout Bank Details",
            actionId = "ACTION_UPDATE_BANK_DETAILS",
            description = "Saves verified banking coordinates with end-to-end encryption"
        ),

        // 23. Copyright (3)
        AppControlItem(
            id = 53,
            domain = "Copyright",
            labelName = "Label 53: Audio & Video Digital Fingerprint Scanner",
            buttonName = "Button 53: Scan Video for Copyright Infringement",
            actionId = "ACTION_SCAN_COPYRIGHT",
            description = "Compares audio waveforms and frames against licensed database"
        ),
        AppControlItem(
            id = 54,
            domain = "Copyright",
            labelName = "Label 54: Copyright Claims Registry & Infringement Notices",
            buttonName = "Button 54: File Copyright Notice / Dispute",
            actionId = "ACTION_FILE_COPYRIGHT_DISPUTE",
            description = "Submits formal DMCA / copyright claim or responds to notice"
        ),
        AppControlItem(
            id = 55,
            domain = "Copyright",
            labelName = "Label 55: Creator Copyright Strike & Policy Warning Counter",
            buttonName = "Button 55: File Counter-Notification Appeal",
            actionId = "ACTION_FILE_COUNTER_NOTIFICATION",
            description = "Appeals unwarranted strike with proof of original authorship"
        ),

        // 24. Reports (2)
        AppControlItem(
            id = 56,
            domain = "Reports",
            labelName = "Label 56: User Community Safety & Violation Report Queue",
            buttonName = "Button 56: Report Content or Channel Violation",
            actionId = "ACTION_REPORT_VIOLATION",
            description = "Dispatches priority investigation ticket to safety review team"
        ),
        AppControlItem(
            id = 57,
            domain = "Reports",
            labelName = "Label 57: Zero Tolerance Adult & Minor Protection Policy Standard",
            buttonName = "Button 57: Submit Sanction Review Appeal",
            actionId = "ACTION_SUBMIT_SANCTION_APPEAL",
            description = "Appeals safety actions (reiterates views NEVER cause account bans)"
        ),

        // 25. Admin Dashboard (3)
        AppControlItem(
            id = 58,
            domain = "Admin Dashboard",
            labelName = "Label 58: Master Global Platform Health & Stream Analytics",
            buttonName = "Button 58: Review Flagged Content Queue",
            actionId = "ACTION_REVIEW_FLAGGED_QUEUE",
            description = "Admin console for moderating flagged videos and enforcing rules"
        ),
        AppControlItem(
            id = 59,
            domain = "Admin Dashboard",
            labelName = "Label 59: Special Creator Reward Program Tracker (₹10 Lakh / 1.10B Views)",
            buttonName = "Button 59: Verify ₹10 Lakh Creator Milestone Claim",
            actionId = "ACTION_VERIFY_SPECIAL_REWARD",
            description = "Validates 1,100,000,000 views milestone for ₹10,00,000 payout approval"
        ),
        AppControlItem(
            id = 60,
            domain = "Admin Dashboard",
            labelName = "Label 60: Kokil Murmu Platform Administration & Financial Controls",
            buttonName = "Button 60: Trigger Scheduled 1st Monthly Payout Cycle",
            actionId = "ACTION_TRIGGER_MONTHLY_PAYOUT_CYCLE",
            description = "Simulates the automated 1st-of-month bank payout backend process"
        )
    )

    init {
        // Assert exactly 60 controls exist
        require(ALL_60_CONTROLS.size == 60) {
            "ALL_60_CONTROLS must contain exactly 60 elements, found ${ALL_60_CONTROLS.size}"
        }
    }
}
