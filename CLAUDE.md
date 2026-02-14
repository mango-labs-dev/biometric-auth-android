# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an **Android library module** (`biometric-auth`) that provides a unified biometric authentication API across different Android SDK versions. Package: `dev.mango.labs.auth.biometric`. Written in Kotlin, built with Gradle (Kotlin DSL).

## Build

This is a library module — it is built as part of a parent multi-module Gradle project. SDK versions (`minVersion`, `maxVersion`) are defined in the root project's `extra` properties. Java/Kotlin target: JVM 17.

```bash
# From the root project directory (parent of this module):
./gradlew :biometric-auth:assembleDebug
./gradlew :biometric-auth:assembleRelease
```

## Architecture

The library uses a **version-stratified inheritance chain** to handle biometric API differences across Android versions:

```
BiometricManagerV23 (API 23+, FingerprintManagerCompat + custom BottomSheetDialog)
  └── BiometricManagerV28 (API 28+, android.hardware.biometrics.BiometricPrompt)
        └── BiometricManagerV30 (API 30+, androidx.biometric.BiometricPrompt with DEVICE_CREDENTIAL)
              └── BiometricManager (public API, Builder pattern entry point)
```

- **BiometricManager** — Public-facing class. Uses `BiometricBuilder` (nested) for configuration (title, subtitle, description, negativeButtonText). Routes `authenticate()` calls to the correct version-specific implementation based on `Build.VERSION.SDK_INT`.
- **BiometricCallback** — Abstract class that consumers implement. Has abstract methods for `onAuthenticationSuccessful()`, `onAuthenticationCancelled()`, `onAuthenticationError()` and optional hooks for help, failure, and permission issues.
- **BiometricCallbackV28 / BiometricCallbackV30** — Adapters that bridge platform-specific `AuthenticationCallback` types to the library's `BiometricCallback`.
- **BiometricDialogV23** — Custom `BottomSheetDialog` used as a fingerprint UI fallback on API 23-27 (pre-BiometricPrompt).
- **BiometricUtils** — Static helpers for checking biometric availability, permission status, and a standalone `authenticateUser()` convenience method using AndroidX BiometricPrompt directly.

## Key Dependencies

- `androidx.biometric:biometric` — AndroidX Biometric library (used in V30 path and `BiometricUtils`)
- `androidx.appcompat` — Required for `AppCompatActivity` context
- `com.google.android.material` — Material BottomSheetDialog for V23 fallback UI

## Resources

Layout and drawable resources in `src/main/res/` support the V23 fallback bottom sheet dialog (`view_bottom_sheet.xml`, `bg_bottom_sheet.xml`, `ic_fingerprint.xml`).
