# INDIA SHORTS – Android Implementation Plan & System Architecture

A comprehensive, production-grade roadmap to address device-level video ingestion on real hardware (such as the iQOO Z9 running Android 14 / Funtouch OS), Android 13–16 media permissions architecture, video playback preview, real cloud storage connectivity, and APK/AAB release readiness without fake states.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> **Real Device Execution Context (iQOO Z9 / Funtouch OS 14 / Android 14):**
> Devices from vivo/iQOO running Android 14 feature strict privacy and background process isolation. Standard file path assumptions (`/sdcard/...`) fail because modern Android enforces scoped storage and virtualized content providers (`content://...`). This plan guarantees that video ingestion relies on Android's official zero-permission Photo Picker (`ACTION_PICK_IMAGES`) and persistent URI permissions (`takePersistableUriPermission`) rather than obsolete storage permissions.

- **Confirmed Decision 1: Android Media Picker Architecture**
  - Use `ActivityResultContracts.PickVisualMedia` with `PickVisualMedia.VideoOnly` as the primary ingestion gateway.
  - Pair with persistent URI read permissions (`takePersistableUriPermission(FLAG_GRANT_READ_URI_PERMISSION)`) to ensure selected videos remain accessible across app lifecycles, preview rendering, compression, and uploads.
- **Confirmed Decision 2: Cloud Backend Integrity Gate**
  - No simulated upload success. If cloud storage credentials (e.g. Firebase Cloud Storage bucket or AWS S3 credentials) are unconfigured, the app surfaces a clear diagnostic error banner (`503 Storage Endpoint Disconnected`) with connection instructions.
- **Confirmed Decision 3: Zero Fake Success / Zero Fake Payments**
  - Keep the Owner PNB Wallet and Creator Payouts in strict DEMO/Audit mode, masking account numbers, requiring PIN authentication, and prohibiting fake payment-provider confirmations.

---

## 1. Overview & Core Concept

- **What It Does**: INDIA SHORTS is a global video-sharing platform created by Kokil Murmu, supporting vertical Shorts (9:16) and landscape Long Videos (16:9), multilingual audio indexing, fair organic trending algorithms, and authentic creator monetization.
- **Target Persona**: Creators recording regional culture, village vlogs, comedy, education, music, and original content on modern Android smartphones (Android 13, 14, 15, and 16).
- **Core Value**: Direct, reliable video ingestion from physical hardware (Gallery, Albums, Files) with authentic metadata extraction, native on-device playback preview, and zero fake success states.

---

## 2. User Experience & Visual Design

### Key User Flows

```
┌────────────────────────────────────────────────────────────────────────┐
│                        CREATOR STUDIO UPLOAD FLOW                      │
└────────────────────────────────────────────────────────────────────────┘

 [1. Select Format]   ──>  Shorts (9:16 Vertical) OR Long Video (16:9)
         │
         ▼
 [2. Choose Video]    ──>  Tap "Select Video via Android Photo Picker"
         │                 (or "Browse Albums" fallback)
         │
         ├─── If 0 videos found on device:
         │    └─ Displays "No Videos Found" guidance banner with instructions:
         │       • Record MP4 using camera
         │       • Transfer/save video to Gallery
         │
         └─── User selects video from device Gallery / Albums:
              ├─ Resolves display name: e.g. "VID_20261104_1705.mp4"
              ├─ Formats file size: e.g. "24.5 MB"
              ├─ Computes real duration: e.g. "0:45"
              └─ Extracts video first-frame bitmap for thumbnail card
         │
         ▼
 [3. Playback Preview]──>  Interactive VideoView with MediaController controls
         │                 (Plays actual content:// stream on device screen)
         │
         ▼
 [4. Video Taxonomy]  ──>  Title, Description, Hashtags, 8 Categories, 20 Languages
         │
         ▼
 [5. Publish Video]   ──>  Connects to Backend Cloud Storage Gate:
                           ├─ If Cloud Connected: Streams chunked binary to bucket
                           └─ If Cloud Disconnected: Stops with clear 503 error
```

### Visual Identity & Theme
- **Color Palette**: Modern Indian Tricolor & High-Contrast Dark Canvas:
  - Deep Navy Canvas (`#0A0F1D`) & Card Surface (`#151C2F`)
  - Saffron Primary (`#FF9933` / `#FF6D00`) for action buttons, progress, and highlights
  - India Green / Emerald (`#138808` / `#00A86B`) for certified status, success states, and monetization
  - Accent Red (`#E53935`) for errors, dismissals, and cancelled states
- **Component Ergonomics**: 48dp+ minimum touch targets, clean Material 3 cards, responsive adaptive columns for Mobile (<600dp), Tablet (600–959dp), and Desktop (≥960dp).

---

## 3. Key Product Decisions & Trade-Offs

### Decision 1: Android 13–16 Media Permission Architecture
- **Chosen Approach**: Use Android's official Photo Picker (`ActivityResultContracts.PickVisualMedia` targeting `VideoOnly`) and `takePersistableUriPermission`.
- **Why**: 
  - On Android 13 (API 33), Android 14 (API 34, iQOO Z9), Android 15 (API 35), and Android 16, Google Play policy strictly prohibits requesting broad storage permissions (`READ_EXTERNAL_STORAGE` or broad media access) unless the app is an enterprise file manager.
  - The Photo Picker grants zero-permission access exclusively to the single video file the user chooses, satisfying both security and privacy constraints on real hardware.
- **Fallback**: For custom file managers or older OS builds, `ActivityResultContracts.GetContent()` with MIME type `video/*` is provided as an explicit "Browse Albums" option.

### Decision 2: Playback Preview Engine
- **Chosen Approach**: Native Android `VideoView` wrapped in Compose `AndroidView`, bound to `content://` URI with an attached `MediaController`.
- **Why**: Hardware-accelerated decoding directly via Android's native stagefright / MediaCodec framework without third-party heavy player overhead, ensuring instant playback on physical devices like the iQOO Z9.

### Decision 3: Empty-Device Guidance
- **Chosen Approach**: Reactive querying of `MediaStore.Video.Media.EXTERNAL_CONTENT_URI`. If 0 videos are present, a persistent dismissible instruction card explains how to record with the camera or transfer an MP4 file.
- **Why**: Real devices fresh out of the box or virtual emulators may lack local media files. Creators must understand why the picker is empty and how to proceed.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                   INDIA SHORTS - DATA & INGESTION ARCHITECTURE         │
└────────────────────────────────────────────────────────────────────────┘

 [Android UI / Compose Screen]
      │
      ├─ UploadScreen.kt
      │    ├─ PickVisualMedia / GetContent launcher
      │    ├─ VideoView (with MediaController & looping preview)
      │    ├─ Selected Metadata Card (Name, Size, Duration, Bitmap)
      │    └─ Cloud Storage Connection Banner
      │
 [State Layer / ViewModel]
      │
      ├─ IndiaShortsViewModel.kt
      │    ├─ selectedVideoUri: StateFlow<String?>
      │    ├─ selectedVideoDurationText: StateFlow<String?>
      │    ├─ selectedVideoThumbnail: StateFlow<Bitmap?>
      │    ├─ deviceVideoCount: StateFlow<Int?>
      │    ├─ processSelectedUri(context, uri)
      │    └─ startVideoUpload()
      │
 [Android Framework & Content Resolver]
      │
      ├─ ContentResolver.takePersistableUriPermission()
      ├─ OpenableColumns (DISPLAY_NAME, SIZE)
      ├─ MediaMetadataRetriever (METADATA_KEY_DURATION, frameAtTime)
      └─ MediaStore.Video.Media query (Device availability check)
      │
 [Cloud Backend Gateway (Requirement 5 & 7)]
      │
      ├─ Authenticated Cloud Storage Endpoint (Firebase Storage / AWS S3)
      │    ├─ If Configured: Chunked upload & Content ID indexing
      │    └─ If Disconnected: Real diagnostic error (Zero fake success)
```

---

## 5. Required Tools & Missing Configurations

### Currently Ready:
- Android Gradle Toolchain with AGP 9.1.1, Kotlin 2.2.10, Jetpack Compose, and Material 3.
- `app/build.gradle.kts` release signing config (`signingConfigs.create("release")`) and debug signing.
- Complete 60 Labels and 60 Buttons in `ControlsCatalog.kt`.
- Passing unit test suite (`:app:testDebugUnitTest`).

### Missing Configuration for Real Cloud Publishing:
To upload videos to a real cloud storage bucket (beyond on-device staging and preview):
1. **Cloud Storage Bucket**: A live cloud storage provider (e.g. Firebase Cloud Storage or AWS S3) must be provisioned.
2. **Secrets / Credentials**: Storage bucket URL and authentication token configured in the AI Studio Secrets panel.
*(Until configured, the application strictly displays the 503 Cloud Disconnected error as required to avoid fake success states).*

### Building Real APK and Play Store AAB:
1. **Debug / Release APK Build**:
   - `gradle :app:assembleDebug` builds the installable Android APK (`app/build/outputs/apk/debug/app-debug.apk`).
   - `gradle :app:assembleRelease` builds the release signed APK using the environment upload keystore.
2. **Play Store Android App Bundle (AAB)**:
   - `gradle :app:bundleRelease` generates the official Google Play store bundle (`app/build/outputs/bundle/release/app-release.aab`).
   *(These tasks can be executed directly via Gradle; no claim of creation will be made until Gradle returns success).*
