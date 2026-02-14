package dev.mango.labs.auth.biometric

interface BiometricCallback {
  fun onAuthenticationSuccessful()
  fun onAuthenticationError(errorCode: Int, errString: CharSequence)
  fun onAuthenticationCancelled() {}
  fun onAuthenticationFailed() {}
}
