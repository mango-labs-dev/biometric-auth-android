package dev.mango.labs.auth.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG

object BiometricUtils {

  fun canAuthenticate(context: Context): Int {
    return BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG)
  }

  fun isBiometricAvailable(context: Context): Boolean {
    return canAuthenticate(context) == BiometricManager.BIOMETRIC_SUCCESS
  }
}
