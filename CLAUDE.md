# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an **Android library module** (`biometric-auth`) that provides a unified biometric authentication API using `androidx.biometric`. Package: `dev.mango.labs.auth.biometric`. Written in Kotlin, built with Gradle (Kotlin DSL).

## Build

This is a library module — it is built as part of a parent multi-module Gradle project. `compileSdk` is defined in the root project's `extra` properties. `minSdk` is hardcoded to 23 in this module. Java/Kotlin target: JVM 17.

```bash
# From the root project directory (parent of this module):
./gradlew :biometric-auth:assembleDebug
./gradlew :biometric-auth:assembleRelease
```

## Architecture

The library uses a single flat class backed entirely by `androidx.biometric.BiometricPrompt`, which handles all API differences internally (API 23-36):

- **BiometricAuthManager** — Public-facing class with a `Builder` pattern for configuration (title, subtitle, description, negativeButtonText, allowDeviceCredential). Calls `authenticate(callback)` to show the system biometric prompt.
- **BiometricCallback** — Kotlin interface that consumers implement. Methods: `onAuthenticationSuccessful()`, `onAuthenticationError()`, `onAuthenticationCancelled()` (default no-op), `onAuthenticationFailed()` (default no-op).
- **BiometricUtils** — Object with `canAuthenticate(context)` and `isBiometricAvailable(context)` for pre-auth availability checks.

## Key Dependencies

- `androidx.biometric:biometric` — AndroidX Biometric library (handles all API levels)
- `androidx.core:core-ktx` — Kotlin extensions for Android core APIs
