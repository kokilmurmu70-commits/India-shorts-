package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoCategory
import com.example.model.VideoType
import com.example.ui.theme.*
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun UploadScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("Traditional Village Harvest Celebration") }
    var description by remember { mutableStateOf("Original village lifestyle and harvest celebration recorded in Santhal Parganas, India. #Santali #VillageVlog #IndiaShorts") }
    var selectedCategory by remember { mutableStateOf(VideoCategory.VILLAGE_VLOG) }
    var selectedLanguage by remember { mutableStateOf("Santali") }
    var customTags by remember { mutableStateOf("#IndiaShorts #VillageLife #Culture") }

    val uploadProgress by viewModel.uploadProgress.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val processingStep by viewModel.processingStep.collectAsState()
    val backendError by viewModel.backendStorageError.collectAsState()
    val isBackendConnected by viewModel.isBackendStorageConnected.collectAsState()

    val selectedVideoUri by viewModel.selectedVideoUri.collectAsState()
    val selectedVideoName by viewModel.selectedVideoName.collectAsState()
    val selectedVideoDuration by viewModel.selectedVideoDurationText.collectAsState()
    val selectedVideoSize by viewModel.selectedVideoSizeText.collectAsState()
    val selectedVideoFormat by viewModel.selectedVideoFormat.collectAsState()
    val selectedVideoThumbnail by viewModel.selectedVideoThumbnail.collectAsState()
    val videoSelectionError by viewModel.videoSelectionError.collectAsState()
    val deviceVideoCount by viewModel.deviceVideoCount.collectAsState()
    val showNoVideosGuidance by viewModel.showNoVideosGuidance.collectAsState()
    val isDemoVideoLoaded by viewModel.isDemoVideoLoaded.collectAsState()

    val selectedThumbnailUri by viewModel.selectedThumbnailUri.collectAsState()
    val selectedVideoType by viewModel.selectedUploadVideoType.collectAsState()
    val isPreviewPlaying by viewModel.isPreviewPlaying.collectAsState()
    val aiThumbnailTitle by viewModel.generatedThumbnailTitle.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.checkDeviceVideosAvailable(context)
    }

    // Official AndroidX Photo Picker (Zero-permission, ACTION_PICK_IMAGES compatible on Android 13-16 & backward compatible)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.processSelectedUri(context, uri)
        }
    }

    // Fallback document/content picker for legacy devices if Photo Picker is unavailable
    val fallbackVideoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.processSelectedUri(context, uri)
        }
    }

    fun launchOfficialPhotoPicker() {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(context)) {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
            )
        } else {
            fallbackVideoPickerLauncher.launch("video/*")
        }
    }

    // Custom Thumbnail picker
    val thumbnailPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setSelectedThumbnail(uri.toString())
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Card
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
                            text = "INDIA SHORTS Creator Studio",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            color = if (isBackendConnected) EmeraldSecondary.copy(alpha = 0.2f) else AccentRed.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isBackendConnected) "CLOUD CONNECTED" else "CLOUD UNCONFIGURED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isBackendConnected) EmeraldSecondary else AccentRed,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Text(
                        text = "Authentic Video Ingestion • Phone Gallery & Albums • Duration & Metadata",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Guidance banner if no video is found on device (Requirement: "If no video is found, show a clear message explaining how to add a video to the device")
        item {
            AnimatedVisibility(visible = showNoVideosGuidance || deviceVideoCount == 0) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("no_videos_guidance_banner")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = SaffronPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "No Videos Found on This Device",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { viewModel.toggleNoVideosGuidance(false) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "To upload your first video to INDIA SHORTS from your Android device:\n" +
                                    "1. Open your phone Camera and record a short or long video (MP4 format).\n" +
                                    "2. Or download/transfer a video via WhatsApp, Telegram, or Files to your Gallery.\n" +
                                    "3. Tap 'Open Gallery' below to select your newly saved video.\n" +
                                    "• Supported formats: MP4, MKV, 3GP, WEBM, MOV.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Error message if no supported videos or unreadable format (Requirement 6 & 8)
        item {
            AnimatedVisibility(visible = videoSelectionError != null) {
                Surface(
                    color = AccentRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_selection_error_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = AccentRed, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Video Selection Error",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = AccentRed
                            )
                            Text(
                                text = videoSelectionError ?: "Unsupported video format or unreadable file.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 15.sp
                            )
                        }
                        IconButton(onClick = { viewModel.clearVideoSelection() }) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = AccentRed, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // 8. Clear Error Display if Backend Storage is Not Connected (Requirement 8)
        item {
            AnimatedVisibility(visible = backendError != null) {
                Surface(
                    color = AccentRed.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("backend_storage_error_banner")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = AccentRed, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Backend Cloud Storage Not Connected",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = AccentRed
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = backendError ?: "Cloud Storage API requires configuration. Upload halted to prevent fake success messages.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.retryBackendStorageConnection() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                            ) {
                                Text("Diagnose Connection", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Button(
                                onClick = {
                                    viewModel.startVideoUpload(
                                        title = title,
                                        description = description,
                                        category = selectedCategory,
                                        language = selectedLanguage,
                                        allowLocalStagingOnly = true
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                            ) {
                                Text("Stage for On-Device Preview", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 1, 2, 3, 4, 7. Video Picker from Phone Gallery & Demo Option
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
                            text = "1. Phone Gallery & Video Format",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )

                        // 7. Demo Video Option for Testing UI
                        OutlinedButton(
                            onClick = { viewModel.loadDemoVideo() },
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier
                                .height(30.dp)
                                .testTag("btn_load_demo_video")
                        ) {
                            Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(14.dp), tint = EmeraldSecondary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Demo Video for Testing", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Video Type Switcher: Shorts (9:16) vs Long Video (16:9)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = if (selectedVideoType == VideoType.SHORT) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setUploadVideoType(VideoType.SHORT) }
                                .testTag("type_selector_short")
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "📱 Shorts (9:16)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (selectedVideoType == VideoType.SHORT) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Vertical Fast Feed",
                                    fontSize = 10.sp,
                                    color = if (selectedVideoType == VideoType.SHORT) Color.White.copy(alpha = 0.8f) else TextSecondary
                                )
                            }
                        }

                        Surface(
                            color = if (selectedVideoType == VideoType.LONG) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setUploadVideoType(VideoType.LONG) }
                                .testTag("type_selector_long")
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🎬 Long Video (16:9)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (selectedVideoType == VideoType.LONG) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Landscape Full Player",
                                    fontSize = 10.sp,
                                    color = if (selectedVideoType == VideoType.LONG) Color.White.copy(alpha = 0.8f) else TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gallery & Albums Picker Action Box
                    // Android Official Photo Picker Action Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavySurface)
                            .border(1.dp, SaffronPrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .clickable { launchOfficialPhotoPicker() }
                            .padding(12.dp)
                            .testTag("btn_pick_gallery_video"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = "Pick Video from Gallery",
                                tint = SaffronPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Select Video via Android Photo Picker",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "ACTION_PICK_IMAGES (Zero broad permissions) • MP4, MKV, 3GP, WEBM",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                            if (isDemoVideoLoaded) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    color = EmeraldSecondary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "DEMO VIDEO LOADED (Testing Mode)",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Explicit Dual Selection Options: Android Photo Picker & Albums Explorer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { launchOfficialPhotoPicker() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_open_gallery")
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Photo Picker", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = {
                                fallbackVideoPickerLauncher.launch("video/*")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_open_albums")
                        ) {
                            Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Browse Albums", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    // 4. Display Filename, Thumbnail, Duration, and Size
                    if (selectedVideoName != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("selected_video_metadata_card")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Thumbnail display: extracted bitmap or fallback icon
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(NavyDeep),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selectedVideoThumbnail != null) {
                                        androidx.compose.foundation.Image(
                                            bitmap = selectedVideoThumbnail!!.asImageBitmap(),
                                            contentDescription = "Extracted Video Thumbnail",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Movie,
                                            contentDescription = "Video Thumbnail",
                                            tint = SaffronPrimary,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = selectedVideoName ?: "Video File",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "⏱️ Duration: ${selectedVideoDuration ?: "0:30"}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "💾 ${selectedVideoSize ?: "18 MB"}",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Format: ${selectedVideoFormat ?: "video/mp4"}",
                                        fontSize = 9.sp,
                                        color = EmeraldSecondary
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.clearVideoSelection() },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Clear Selection",
                                        tint = AccentRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4 & 6. Video Processing & Interactive Playback Preview (Requirement 4 & 6)
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
                            text = "2. Playback Preview & Processing",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = processingStep,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Interactive Player Preview Screen with real VideoView or simulated engine
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (selectedVideoType == VideoType.SHORT) 220.dp else 160.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(NavyDeep, NavySurface)
                                )
                            )
                            .clickable { viewModel.togglePreviewPlayback() }
                            .testTag("preview_player_box"),
                        contentAlignment = Alignment.Center
                    ) {
                        // If preview is playing and URI is a real Android URI, render VideoView
                        if (isPreviewPlaying && selectedVideoUri != null) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                androidx.compose.ui.viewinterop.AndroidView(
                                    factory = { ctx ->
                                        android.widget.VideoView(ctx).apply {
                                            layoutParams = android.view.ViewGroup.LayoutParams(
                                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                                android.view.ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                            try {
                                                val uri = Uri.parse(selectedVideoUri)
                                                setVideoURI(uri)
                                                setOnPreparedListener { mp ->
                                                    mp.isLooping = true
                                                    mp.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
                                                    start()
                                                }
                                                setOnErrorListener { _, _, _ ->
                                                    true
                                                }
                                            } catch (e: Exception) {
                                                // Fallback gracefully
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Overlay button to pause/stop playback preview on real devices
                                IconButton(
                                    onClick = { viewModel.togglePreviewPlayback() },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .size(36.dp)
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Pause,
                                        contentDescription = "Pause Preview",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        } else {
                            // Video poster or frame preview
                            if (selectedVideoThumbnail != null) {
                                androidx.compose.foundation.Image(
                                    bitmap = selectedVideoThumbnail!!.asImageBitmap(),
                                    contentDescription = "Video Poster Preview",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = if (selectedVideoThumbnail != null) 0.45f else 0.2f))
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.6f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isPreviewPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                        contentDescription = "Play Preview",
                                        tint = Color.White,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isPreviewPlaying) "Playing Staged Video Preview" else "Tap to Play Video Preview",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${selectedVideoType.name} • ${selectedVideoName ?: "No file loaded"} • ${selectedVideoDuration ?: "0:30"}",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Surface(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "${selectedVideoDuration ?: "0:30"} Duration",
                                fontSize = 9.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Thumbnail Selection (Gallery Image or AI Cover) (Requirement 4)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3. Video Thumbnail & Poster Cover",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Custom Gallery Thumbnail Button
                        Button(
                            onClick = {
                                thumbnailPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_pick_thumbnail")
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick Custom Photo", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        // AI Cover Generator Button
                        Button(
                            onClick = { viewModel.generateAiThumbnail() },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_10")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("AI Smart Cover", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (selectedThumbnailUri != null) "✓ Custom Photo Attached: ${selectedThumbnailUri?.take(28)}..."
                            else "Active Cover: $aiThumbnailTitle",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        // 3. Metadata Fields: Title, Description, Hashtags, Category (Requirement 3)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Video Details & Content Taxonomy",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Video Title *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_title_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_description_input"),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customTags,
                        onValueChange = { customTags = it },
                        label = { Text("Hashtags (e.g. #IndiaShorts #Culture)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_hashtags_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Content Category Picker
                    Text("Select Category:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(VideoCategory.values().size) { idx ->
                            val cat = VideoCategory.values()[idx]
                            val isSel = cat == selectedCategory
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat.displayName, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaffronPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Language Picker
                    Text("Primary Language:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val langs = listOf("Santali", "Hindi", "Khortha", "Bhojpuri", "English", "Telugu", "Tamil", "Bengali")
                        items(langs.size) { idx ->
                            val l = langs[idx]
                            val isSel = l == selectedLanguage
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedLanguage = l },
                                label = { Text(l, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldSecondary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // 5. Upload Progress, Cancel Option & Publish Action (Requirement 5 & 7)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isUploading) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Uploading & Staging: $uploadProgress%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary
                            )
                            // Cancel Upload Action (Requirement 5)
                            TextButton(
                                onClick = { viewModel.cancelUpload() },
                                colors = ButtonDefaults.textButtonColors(contentColor = AccentRed),
                                modifier = Modifier.testTag("btn_cancel_upload")
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Cancel Upload", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { uploadProgress / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = SaffronPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = processingStep,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Button(
                        onClick = {
                            viewModel.startVideoUpload(
                                title = title,
                                description = description,
                                category = selectedCategory,
                                language = selectedLanguage
                            )
                        },
                        enabled = !isUploading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SaffronPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_11")
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUploading) "Processing Upload Pipeline..." else "Button 11: Publish Video Now",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
