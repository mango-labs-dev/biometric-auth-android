package dev.mango.labs.auth.biometric

import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthManager private constructor(builder: Builder) {

  private val activity: FragmentActivity = builder.activity
  private val title: String = builder.title
  private val subtitle: String = builder.subtitle
  private val description: String = builder.description
  private val negativeButtonText: String = builder.negativeButtonText
  private val allowDeviceCredential: Boolean = builder.allowDeviceCredential

  internal fun createAuthCallback(callback: BiometricCallback): BiometricPrompt.AuthenticationCallback {
    return object : BiometricPrompt.AuthenticationCallback() {
      override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        callback.onAuthenticationSuccessful()
      }

      override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        if (errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
          errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
          callback.onAuthenticationCancelled()
        } else {
          callback.onAuthenticationError(errorCode, errString)
        }
      }

      override fun onAuthenticationFailed() {
        callback.onAuthenticationFailed()
      }
    }
  }

  fun authenticate(callback: BiometricCallback) {
    val executor = ContextCompat.getMainExecutor(activity)
    val authCallback = createAuthCallback(callback)
    val biometricPrompt = BiometricPrompt(activity, executor, authCallback)

    val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
      .setTitle(title)

    if (subtitle.isNotEmpty()) {
      promptInfoBuilder.setSubtitle(subtitle)
    }
    if (description.isNotEmpty()) {
      promptInfoBuilder.setDescription(description)
    }

    if (allowDeviceCredential) {
      promptInfoBuilder.setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    } else {
      promptInfoBuilder.setNegativeButtonText(negativeButtonText)
      promptInfoBuilder.setAllowedAuthenticators(BIOMETRIC_STRONG)
    }

    biometricPrompt.authenticate(promptInfoBuilder.build())
  }

  class Builder(internal val activity: FragmentActivity) {
    internal var title: String = ""
    internal var subtitle: String = ""
    internal var description: String = ""
    internal var negativeButtonText: String = ""
    internal var allowDeviceCredential: Boolean = false

    fun setTitle(title: String): Builder {
      this.title = title
      return this
    }

    fun setSubtitle(subtitle: String): Builder {
      this.subtitle = subtitle
      return this
    }

    fun setDescription(description: String): Builder {
      this.description = description
      return this
    }

    fun setNegativeButtonText(negativeButtonText: String): Builder {
      this.negativeButtonText = negativeButtonText
      return this
    }

    fun setAllowDeviceCredential(allow: Boolean): Builder {
      this.allowDeviceCredential = allow
      return this
    }

    fun build(): BiometricAuthManager {
      check(title.isNotEmpty()) { "Title is required" }
      if (!allowDeviceCredential) {
        check(negativeButtonText.isNotEmpty()) {
          "Negative button text is required when device credential is not allowed"
        }
      }
      return BiometricAuthManager(this)
    }
  }
}
