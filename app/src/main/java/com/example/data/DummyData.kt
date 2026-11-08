package com.example.data

import com.example.model.*

object DummyData {

    val SUPPORTED_LANGUAGES = listOf(
        SupportedLanguage("hi", "Hindi", "हिन्दी", "India"),
        SupportedLanguage("en", "English", "English", "Global"),
        SupportedLanguage("te", "Telugu", "తెలుగు", "Andhra / Telangana"),
        SupportedLanguage("sat", "Santali", "ᱥᱟᱱᱛᱟᱲᱤ", "Jharkhand / Bengal / Odisha"),
        SupportedLanguage("khr", "Khortha", "खोरठा", "Jharkhand"),
        SupportedLanguage("gu", "Gujarati", "ગુજરાતી", "Gujarat"),
        SupportedLanguage("bho", "Bhojpuri", "भोजपुरी", "Bihar / UP"),
        SupportedLanguage("mai", "Maithili", "मैथिली", "Mithila / Bihar"),
        SupportedLanguage("mag", "Magahi", "मगही", "Bihar"),
        SupportedLanguage("ta", "Tamil", "தமிழ்", "Tamil Nadu"),
        SupportedLanguage("zh", "Chinese", "中文", "Worldwide"),
        SupportedLanguage("mr", "Marathi", "मराठी", "Maharashtra"),
        SupportedLanguage("bn", "Bengali", "বাংলা", "West Bengal / Tripura"),
        SupportedLanguage("pa", "Punjabi", "ਪੰਜਾਬੀ", "Punjab"),
        SupportedLanguage("kn", "Kannada", "ಕನ್ನಡ", "Karnataka"),
        SupportedLanguage("ml", "Malayalam", "മലയാളം", "Kerala"),
        SupportedLanguage("or", "Odia", "ଓଡ଼ିଆ", "Odisha"),
        SupportedLanguage("as", "Assamese", "অসমীয়া", "Assam"),
        SupportedLanguage("ur", "Urdu", "اردو", "India / Global"),
        SupportedLanguage("sa", "Sanskrit", "संस्कृतम्", "India")
    )

    val INITIAL_USERS = listOf(
        UserProfile(
            id = "owner_kokil",
            name = "Kokil Murmu",
            username = "@kokilmurmu",
            mobileNumber = "+91 98765 43210",
            bio = "Official Founder & Platform Owner of INDIA SHORTS. Championing Indian & global video creators worldwide.",
            avatarColorHex = 0xFFFF6D00,
            isCreator = true,
            isOwner = true,
            followersCount = 485000,
            followingCount = 42,
            verifiedBadge = true
        ),
        UserProfile(
            id = "creator_birsa",
            name = "Birsa Soren",
            username = "@birsavillagevlogs",
            mobileNumber = "+91 94312 00001",
            bio = "Authentic Santali village lifestyle, farming vlogs and rural culinary recipes from Santhal Parganas.",
            avatarColorHex = 0xFF00A86B,
            isCreator = true,
            isOwner = false,
            followersCount = 38400,
            followingCount = 120,
            verifiedBadge = true,
            monetizationStatus = MonetizationStatus.MONETIZED
        ),
        UserProfile(
            id = "creator_priya",
            name = "Priya Sharma",
            username = "@priyacomedy_hindi",
            mobileNumber = "+91 98111 22334",
            bio = "Everyday desi family comedy & relatable sketches in Hindi and Bhojpuri! Making you smile daily.",
            avatarColorHex = 0xFFE11D48,
            isCreator = true,
            isOwner = false,
            followersCount = 92500,
            followingCount = 95,
            verifiedBadge = true,
            monetizationStatus = MonetizationStatus.IN_PROGRESS // Non-monetized creator: JOIN button must NOT show
        )
    )

    val INITIAL_SHORTS = listOf(
        VideoItem(
            id = "short_1",
            title = "Santali Traditional Sohrai Dance in Village 🌾✨",
            description = "Celebration of harvest with traditional tumdak and tamak beats in Dumka, Jharkhand. Created by original village performers. #Santali #TribalCulture #VillageVlog #IndiaShorts",
            creatorName = "Birsa Soren",
            creatorUsername = "@birsavillagevlogs",
            creatorAvatarColor = 0xFF00A86B,
            videoType = VideoType.SHORT,
            category = VideoCategory.CULTURAL,
            language = "Santali",
            durationText = "0:45",
            viewsCount = 145000,
            eligibleViewsCount = 138000,
            likesCount = 28400,
            commentsCount = 1240,
            sharesCount = 6800,
            soundTitle = "Traditional Santali Flute & Tumdak Beat",
            soundArtist = "Kokil Audio Lab",
            hashtags = listOf("#Santali", "#VillageVlog", "#Culture", "#IndiaShorts"),
            gradientStartHex = 0xFF14532D,
            gradientEndHex = 0xFF064E3B,
            isLiked = true,
            isFollowingCreator = true,
            copyrightCertified = true,
            captionsText = "Celebrating the grand Sohrai harvest festival with unity, rhythm and joyful dance across our rural Santhal village."
        ),
        VideoItem(
            id = "short_2",
            title = "Desi Mom vs Online Delivery Package 😂📦",
            description = "When my courier arrives at 7 AM on Sunday! Relatable Indian home comedy sketch. #Comedy #DesiMemes #Hindi #IndiaShorts",
            creatorName = "Priya Sharma",
            creatorUsername = "@priyacomedy_hindi",
            creatorAvatarColor = 0xFFE11D48,
            videoType = VideoType.SHORT,
            category = VideoCategory.COMEDY,
            language = "Hindi",
            durationText = "0:32",
            viewsCount = 389000,
            eligibleViewsCount = 365000,
            likesCount = 82100,
            commentsCount = 3450,
            sharesCount = 19200,
            soundTitle = "Desi Comedy Laugh Beat 2026",
            soundArtist = "Authorized Beats India",
            hashtags = listOf("#Comedy", "#Desi", "#Hindi", "#Relatable"),
            gradientStartHex = 0xFF831843,
            gradientEndHex = 0xFF4C0519,
            isLiked = false,
            isFollowingCreator = false,
            copyrightCertified = true,
            captionsText = "Mom: 'Who is sending you boxes early morning? Did you order more clothes again?'"
        ),
        VideoItem(
            id = "short_3",
            title = "Fresh Organic Turmeric Harvest in Village Garden 🌱",
            description = "Processing raw golden turmeric directly from mother earth. Pure natural farming techniques. #VillageLife #Organic #Khortha #Jharkhand",
            creatorName = "Kokil Murmu",
            creatorUsername = "@kokilmurmu",
            creatorAvatarColor = 0xFFFF6D00,
            videoType = VideoType.SHORT,
            category = VideoCategory.VILLAGE_VLOG,
            language = "Khortha",
            durationText = "0:58",
            viewsCount = 682000,
            eligibleViewsCount = 640000,
            likesCount = 142000,
            commentsCount = 4200,
            sharesCount = 28000,
            soundTitle = "Morning Birds & Natural Village Breeze",
            soundArtist = "Nature Ambient Sounds",
            hashtags = listOf("#Organic", "#VillageVlog", "#Nature", "#KokilMurmu"),
            gradientStartHex = 0xFF78350F,
            gradientEndHex = 0xFF451A03,
            isLiked = true,
            isFollowingCreator = true,
            copyrightCertified = true,
            captionsText = "Nothing matches the aroma of newly harvested organic turmeric. Real health from our village soil."
        ),
        VideoItem(
            id = "short_4",
            title = "5 Secrets to Clear Any Tech Coding Interview 💻🚀",
            description = "Stop memorizing algorithms blindly! Learn pattern recognition in DSA with live examples. #Education #Tech #English #Coding",
            creatorName = "Tech Guru Rahul",
            creatorUsername = "@techrahul",
            creatorAvatarColor = 0xFF2563EB,
            videoType = VideoType.SHORT,
            category = VideoCategory.EDUCATIONAL,
            language = "English",
            durationText = "0:50",
            viewsCount = 210000,
            eligibleViewsCount = 198000,
            likesCount = 45000,
            commentsCount = 980,
            sharesCount = 12500,
            soundTitle = "Inspiring Future Tech Lo-Fi",
            soundArtist = "Global Creative Commons",
            hashtags = listOf("#Coding", "#Education", "#Interview", "#Tech"),
            gradientStartHex = 0xFF1E3A8A,
            gradientEndHex = 0xFF0F172A,
            isLiked = false,
            isFollowingCreator = true,
            copyrightCertified = true,
            captionsText = "Pattern 1: Two pointers for sorted arrays. Master the pattern, not just the isolated code."
        ),
        VideoItem(
            id = "short_5",
            title = "Exploring Spiti Valley Hidden Monastery in Winter ❄️🏔️",
            description = "Subzero temperatures at 14,000 feet! Witness the ancient prayer flags fluttering over snowy peaks. #Travel #Himalayas #IndiaTravel",
            creatorName = "Wanderlust Ananya",
            creatorUsername = "@ananyatravels",
            creatorAvatarColor = 0xFF0891B2,
            videoType = VideoType.SHORT,
            category = VideoCategory.TRAVEL,
            language = "Hindi",
            durationText = "0:40",
            viewsCount = 450000,
            eligibleViewsCount = 420000,
            likesCount = 98000,
            commentsCount = 2100,
            sharesCount = 15300,
            soundTitle = "Himalayan Wind Chimes & Flute",
            soundArtist = "Mountain Echoes",
            hashtags = listOf("#Travel", "#Spiti", "#Himalayas", "#Winter"),
            gradientStartHex = 0xFF164E63,
            gradientEndHex = 0xFF083344,
            isLiked = false,
            isFollowingCreator = false,
            copyrightCertified = true,
            captionsText = "Spiti in winter feels like standing on another planet. Quiet, timeless, and completely sacred."
        )
    )

    val INITIAL_LONG_VIDEOS = listOf(
        VideoItem(
            id = "long_1",
            title = "Full Village Infrastructure & Bamboo Eco-House Tour 🏡🌾",
            description = "Welcome to our complete village tour! See how we designed a thermally insulated bamboo and clay home using traditional regional techniques and solar backup. Hosted by Kokil Murmu.",
            creatorName = "Kokil Murmu",
            creatorUsername = "@kokilmurmu",
            creatorAvatarColor = 0xFFFF6D00,
            videoType = VideoType.LONG,
            category = VideoCategory.VILLAGE_VLOG,
            language = "Hindi",
            durationText = "14:28",
            viewsCount = 890000,
            eligibleViewsCount = 850000,
            likesCount = 184000,
            commentsCount = 6500,
            sharesCount = 34000,
            soundTitle = "Original Environment Audio",
            soundArtist = "Kokil Murmu Media",
            hashtags = listOf("#VillageVlog", "#EcoFriendly", "#KokilMurmu", "#Architecture"),
            gradientStartHex = 0xFF312E81,
            gradientEndHex = 0xFF1E1B4B,
            isLiked = true,
            isFollowingCreator = true,
            copyrightCertified = true,
            captionsText = "Today we explore sustainable eco-architecture built purely from local bamboo, clay, and terracotta tiles."
        ),
        VideoItem(
            id = "long_2",
            title = "Complete Android Jetpack Compose Masterclass 2026 📱⚡",
            description = "From beginner to production-grade architecture in Kotlin. We build reactive MVVM state, clean Material 3 UI, and offline-first storage from scratch.",
            creatorName = "Tech Guru Rahul",
            creatorUsername = "@techrahul",
            creatorAvatarColor = 0xFF2563EB,
            videoType = VideoType.LONG,
            category = VideoCategory.EDUCATIONAL,
            language = "English",
            durationText = "38:15",
            viewsCount = 320000,
            eligibleViewsCount = 310000,
            likesCount = 54000,
            commentsCount = 2800,
            sharesCount = 18000,
            soundTitle = "Ambient Developer Lo-Fi",
            soundArtist = "Authorized Code Beats",
            hashtags = listOf("#Android", "#Kotlin", "#Compose", "#Programming"),
            gradientStartHex = 0xFF1E293B,
            gradientEndHex = 0xFF0F172A,
            isLiked = false,
            isFollowingCreator = true,
            copyrightCertified = true,
            captionsText = "In this module, you will learn how StateFlow drives declarative UI recompositions with zero lag."
        )
    )

    val INITIAL_SONGS = listOf(
        SongItem(
            id = "song_1",
            title = "Tumdak & Tamak Sohrai Folk Rhythm",
            artist = "Kokil Murmu Folk Studio",
            language = "Santali",
            duration = "2:45",
            usageCount = "48.2K Shorts",
            isLicensed = true,
            licenseBadge = "Authorized Royalty-Free Certified"
        ),
        SongItem(
            id = "song_2",
            title = "Jharkhand Mati Re (Khortha Anthem)",
            artist = "Khortha Wave Beats",
            language = "Khortha",
            duration = "3:12",
            usageCount = "31.9K Shorts",
            isLicensed = true,
            licenseBadge = "Authorized Creator License"
        ),
        SongItem(
            id = "song_3",
            title = "Desi Dholak & Flute Fiesta",
            artist = "Bhojpuri & Hindi Beats Ensemble",
            language = "Bhojpuri",
            duration = "2:30",
            usageCount = "124.5K Shorts",
            isLicensed = true,
            licenseBadge = "Certified Universal Library"
        ),
        SongItem(
            id = "song_4",
            title = "Carnatic Fusion Acoustic Strings",
            artist = "Dakshin Melody Collective",
            language = "Tamil / Telugu",
            duration = "3:40",
            usageCount = "19.8K Shorts",
            isLicensed = true,
            licenseBadge = "Licensed Indian Heritage Audio"
        )
    )

    val INITIAL_COMMENTS = listOf(
        CommentItem(
            id = "c_1",
            videoId = "short_1",
            authorName = "Sunita Soren",
            content = "Adid gorkit raska! Seeing our Santali culture preserved with such high definition on INDIA SHORTS makes me proud! Jowar Kokil Murmu da!",
            timestamp = "2 hours ago",
            likesCount = 142,
            isCreatorPinned = true,
            creatorReply = "Thank you Sunita! INDIA SHORTS was created specifically to give every regional artist a global stage. - Kokil Murmu"
        ),
        CommentItem(
            id = "c_2",
            videoId = "short_1",
            authorName = "Amit Kumar Verma",
            content = "The music and rhythm are so captivating! Greetings from Ranchi! 🇮🇳✨",
            timestamp = "4 hours ago",
            likesCount = 89
        ),
        CommentItem(
            id = "c_3",
            videoId = "short_2",
            authorName = "Neha Singh",
            content = "This is literally my mom every Sunday morning! Hilarious and 100% accurate 😂",
            timestamp = "1 hour ago",
            likesCount = 64
        )
    )

    val INITIAL_TRANSACTIONS = listOf(
        TransactionRecord(
            id = "txn_101",
            txnId = "PNB-DEMO-20261001-9842",
            type = TransactionType.OWNER_MONTHLY_PAYOUT,
            amountFormatted = "$12,450.00",
            destinationAccount = "Punjab National Bank (Backend Encrypted Vault)",
            timestamp = "2026-10-01 09:15 AM",
            status = TransactionStatus.PROCESSING,
            note = "DEMO DATA: Scheduled Monthly Auto-Payout simulation. Awaiting real payment provider confirmation."
        ),
        TransactionRecord(
            id = "txn_102",
            txnId = "PNB-DEMO-20261018-4419",
            type = TransactionType.OWNER_MANUAL_WITHDRAWAL,
            amountFormatted = "$5,000.00",
            destinationAccount = "Punjab National Bank (Backend Encrypted Vault)",
            timestamp = "2026-10-18 03:42 PM",
            status = TransactionStatus.PENDING,
            note = "DEMO DATA: Owner Manual Withdrawal request. Pending real banking gateway authorization."
        ),
        TransactionRecord(
            id = "txn_103",
            txnId = "CR-DEMO-20261025-1102",
            type = TransactionType.CREATOR_WITHDRAWAL,
            amountFormatted = "$280.00",
            destinationAccount = "Verified Creator Account (Backend Vault)",
            timestamp = "2026-10-25 11:20 AM",
            status = TransactionStatus.PENDING,
            note = "DEMO DATA: Creator Withdrawal request ($200+ Threshold). Pending gateway confirmation."
        )
    )

    val INITIAL_NOTIFICATIONS = listOf(
        AppNotification(
            id = "notif_1",
            title = "Bank Payout Dispatched to PNB",
            message = "Official Monthly Revenue Payout of $12,450.00 was successfully credited to Punjab National Bank A/C ending 5821 for Owner Kokil Murmu.",
            type = NotificationType.WALLET,
            timeAgo = "Yesterday",
            isRead = false
        ),
        AppNotification(
            id = "notif_2",
            title = "Monetization Revenue Calculated",
            message = "New ad revenue settlement: 13.3333% creator share credited. $8 per 10k views allocated.",
            type = NotificationType.MONETIZATION,
            timeAgo = "2 days ago",
            isRead = true
        ),
        AppNotification(
            id = "notif_3",
            title = "Special ₹10 Lakh Reward Milestone Progress",
            message = "Your platform verified views crossed 428,950,000 eligible views towards the 1.10 Billion target!",
            type = NotificationType.REWARD,
            timeAgo = "3 days ago",
            isRead = true
        ),
        AppNotification(
            id = "notif_4",
            title = "Copyright Scanner Passed",
            message = "All uploaded short video clips passed digital fingerprint and audio copyright certification.",
            type = NotificationType.COPYRIGHT,
            timeAgo = "4 days ago",
            isRead = true
        )
    )

    val INITIAL_ADS = listOf(
        AdItem(
            id = "ad_native_1",
            title = "Patanjali Herbal Hair Care",
            sponsorName = "Patanjali Ayurved",
            description = "100% Ayurvedic Kesh Kanti oil for healthy, thick, and lustrous hair naturally.",
            callToAction = "Install App",
            targetUrl = "https://play.google.com",
            adType = "NATIVE_SHORTS",
            isTestAd = true,
            verifiedCpmInr = 45.0
        ),
        AdItem(
            id = "ad_banner_1",
            title = "MakeMyTrip India Vacation Sale",
            sponsorName = "MakeMyTrip India",
            description = "Get up to 40% OFF on domestic flights, trains & hotels across India with code INDIASHORTS.",
            callToAction = "Book Now",
            targetUrl = "https://makemytrip.com",
            adType = "BANNER_HOME",
            isTestAd = true,
            verifiedCpmInr = 35.0
        ),
        AdItem(
            id = "ad_video_1",
            title = "SBI YONO Digital Banking",
            sponsorName = "State Bank of India",
            description = "Instant zero-balance digital savings account with free virtual debit card.",
            callToAction = "Open Account",
            targetUrl = "https://sbi.co.in",
            adType = "VIDEO_PREROLL",
            isTestAd = true,
            verifiedCpmInr = 50.0
        ),
        AdItem(
            id = "ad_sponsor_1",
            title = "Tata Motors EV Revolution",
            sponsorName = "Tata Motors Official",
            description = "Discover the all-new Nexon EV with 465 km long range and fast charging.",
            callToAction = "Explore Nexon EV",
            targetUrl = "https://tatamotors.com",
            adType = "SPONSORED_POST",
            isTestAd = true,
            verifiedCpmInr = 60.0
        )
    )
}
