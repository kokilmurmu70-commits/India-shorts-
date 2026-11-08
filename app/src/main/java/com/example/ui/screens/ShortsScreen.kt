package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoItem
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun ShortsScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    val shorts by viewModel.shortsFeed.collectAsState()
    val currentIndex by viewModel.currentShortIndex.collectAsState()
    val isPlaying by viewModel.isShortPlaying.collectAsState()
    val showSubtitles by viewModel.showSubtitles.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val membershipState by viewModel.creatorMembership.collectAsState()

    var showCommentsSheet by remember { mutableStateOf(false) }
    var showJoinSheet by remember { mutableStateOf(false) }

    val currentVideo = shorts.getOrNull(currentIndex) ?: return

    // Sound disc rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "disc")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "discAngle"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeep),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    var dragTotal = 0f
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            dragTotal += dragAmount
                        },
                        onDragEnd = {
                            if (dragTotal < -100f) {
                                viewModel.nextShort() // Swiped up -> next
                            } else if (dragTotal > 100f) {
                                viewModel.previousShort() // Swiped down -> previous
                            }
                            dragTotal = 0f
                        }
                    )
                }
        ) {
            // Dynamic Backdrop Gradient (Simulated high-res video rendering)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(currentVideo.gradientStartHex),
                                NavyDeep,
                                Color(currentVideo.gradientEndHex)
                            )
                        )
                    )
                    .clickable { viewModel.toggleShortPlayPause() }
            )

        // Center Play/Pause indicator overlay if paused
        if (!isPlaying) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Paused",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Top Controls: Category, Quality badge, Sound badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldSecondary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = currentVideo.category.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${currentVideo.language}",
                        fontSize = 11.sp,
                        color = SaffronLight
                    )
                }
            }

            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.clickable { viewModel.cycleResolution() }
            ) {
                Text(
                    text = "HD 1080p",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Right-Side Interaction Rail (Heart, Comment, Share, Save, Music Disc)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 70.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like (Button 19, Label 19)
            InteractionIcon(
                icon = if (currentVideo.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                label = "${currentVideo.likesCount}",
                tint = if (currentVideo.isLiked) AccentRed else Color.White,
                onClick = { viewModel.toggleLikeCurrentShort() },
                testTag = "btn_19"
            )

            // Comment (Button 21)
            InteractionIcon(
                icon = Icons.Outlined.Comment,
                label = "${currentVideo.commentsCount}",
                tint = Color.White,
                onClick = { showCommentsSheet = true },
                testTag = "btn_21"
            )

            // Share (Button 23)
            InteractionIcon(
                icon = Icons.Outlined.Share,
                label = "${currentVideo.sharesCount}",
                tint = Color.White,
                onClick = { viewModel.executeButtonAction(23) },
                testTag = "btn_23"
            )

            // Save / Watchlist (Button 5)
            InteractionIcon(
                icon = if (currentVideo.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                label = if (currentVideo.isSaved) "Saved" else "Save",
                tint = if (currentVideo.isSaved) AccentGold else Color.White,
                onClick = { viewModel.toggleSaveCurrentShort() },
                testTag = "btn_5"
            )

            // Rotating Music Disc (Label 5)
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.8f))
                    .border(2.dp, SaffronPrimary, CircleShape)
                    .rotate(if (isPlaying) angle else 0f)
                    .clickable { viewModel.navigateTo(AppScreen.MUSIC) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Sound",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Bottom Overlay: Creator info, Title, Hashtags, Audio ticker & Captions
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.82f)
                .padding(start = 16.dp, bottom = 20.dp, end = 8.dp)
        ) {
            // Closed Captions banner if enabled
            if (showSubtitles && currentVideo.captionsText.isNotBlank()) {
                Surface(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "CC: ${currentVideo.captionsText}",
                        color = Color.White,
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Creator Row: Avatar + Name + Follow Button (Button 25)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(currentVideo.creatorAvatarColor)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentVideo.creatorName.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentVideo.creatorName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Filled.Verified,
                            contentDescription = "Verified Creator",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = currentVideo.creatorUsername,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))

                // Follow Button
                Button(
                    onClick = { viewModel.toggleFollowCreator() },
                    modifier = Modifier
                        .height(30.dp)
                        .testTag("btn_25"),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentVideo.isFollowingCreator) Color.White.copy(alpha = 0.2f) else SaffronPrimary
                    ),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = if (currentVideo.isFollowingCreator) "Following" else "Follow",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // REQUIREMENT 1: JOIN BUTTON (Only for Monetized Approved Creators, next to Follow button)
                val isJoinEligible = viewModel.isCreatorJoinEligible(currentVideo.creatorUsername)
                if (isJoinEligible) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = { showJoinSheet = true },
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("btn_join_creator_${currentVideo.creatorUsername}"),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentUser.isSubscribedToCreator) EmeraldSecondary else Color(0xFF0284C7)
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardMembership,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentUser.isSubscribedToCreator) "MEMBER" else "JOIN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            // Video Title & Description
            Text(
                text = currentVideo.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Requirement 1.1: Shorts Feed में Native Ads (Every 2nd or 3rd short has native sponsored card)
            if (currentIndex % 2 == 1) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.recordSimulatedAdImpression("NATIVE_SHORTS") }
                        .testTag("shorts_native_ad_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AccentGold,
                            shape = RoundedCornerShape(3.dp)
                        ) {
                            Text(
                                text = "AD",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sponsored: Patanjali Herbal Care",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "100% Ayurvedic Kesh Kanti oil • AdMob Native Test Unit",
                                fontSize = 9.sp,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = SaffronPrimary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Install",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Hashtags
            Text(
                text = currentVideo.hashtags.joinToString(" "),
                fontSize = 12.sp,
                color = AccentGold,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Sound Track info ticker
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { viewModel.navigateTo(AppScreen.MUSIC) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.MusicNote,
                    contentDescription = "Music",
                    tint = EmeraldLight,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${currentVideo.soundTitle} • ${currentVideo.soundArtist}",
                    fontSize = 11.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

    if (showCommentsSheet) {
        CommentsBottomSheet(
            viewModel = viewModel,
            onDismiss = { showCommentsSheet = false }
        )
    }
}

@Composable
fun InteractionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    viewModel: IndiaShortsViewModel,
    onDismiss: () -> Unit
) {
    val comments by viewModel.comments.collectAsState()
    var commentInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comments (${comments.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Emoji reactions bar (Button 22)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("❤️", "🔥", "🇮🇳", "👏", "😂", "✨").forEach { emoji ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.clickable {
                            viewModel.addComment(emoji)
                        }
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comments List
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .heightIn(max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(comments.size) { idx ->
                    val c = comments[idx]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = c.authorName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (c.isCreatorPinned) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = SaffronPrimary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "PINNED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SaffronPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = c.timestamp,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = c.content,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        if (c.creatorReply != null) {
                            Surface(
                                color = EmeraldSecondary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                            ) {
                                Text(
                                    text = "Creator Reply: ${c.creatorReply}",
                                    fontSize = 11.sp,
                                    color = EmeraldSecondary,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Post new comment row (Button 21)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentInput,
                    onValueChange = { commentInput = it },
                    placeholder = { Text("Add a comment on INDIA SHORTS...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("comment_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (commentInput.isNotBlank()) {
                            viewModel.addComment(commentInput)
                            commentInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SaffronPrimary)
                        .testTag("btn_21")
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Post Comment",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    // REQUIREMENT 3: MEMBERSHIP JOIN BOTTOM SHEET
    if (showJoinSheet) {
        com.example.ui.components.MembershipJoinBottomSheet(
            creatorName = currentVideo.creatorName,
            creatorUsername = currentVideo.creatorUsername,
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
