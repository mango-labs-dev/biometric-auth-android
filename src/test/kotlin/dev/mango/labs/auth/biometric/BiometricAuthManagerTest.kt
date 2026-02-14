package dev.mango.labs.auth.biometric

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BiometricAuthManagerTest {

  private lateinit var activity: FragmentActivity
  private lateinit var callback: BiometricCallback
  private lateinit var authCallback: BiometricPrompt.AuthenticationCallback

  @Before
  fun setUp() {
    activity = Robolectric.buildActivity(FragmentActivity::class.java)
      .create()
      .get()
    callback = mockk(relaxed = true)

    val manager = BiometricAuthManager.Builder(activity)
      .setTitle("Test")
      .setNegativeButtonText("Cancel")
      .build()

    authCallback = manager.createAuthCallback(callback)
  }

  @Test
  fun `authenticate calls onAuthenticationSuccessful on success`() {
    val result = mockk<BiometricPrompt.AuthenticationResult>()

    authCallback.onAuthenticationSucceeded(result)

    verify { callback.onAuthenticationSuccessful() }
  }

  @Test
  fun `authenticate calls onAuthenticationCancelled on ERROR_USER_CANCELED`() {
    authCallback.onAuthenticationError(BiometricPrompt.ERROR_USER_CANCELED, "Canceled")

    verify { callback.onAuthenticationCancelled() }
  }

  @Test
  fun `authenticate calls onAuthenticationCancelled on ERROR_NEGATIVE_BUTTON`() {
    authCallback.onAuthenticationError(BiometricPrompt.ERROR_NEGATIVE_BUTTON, "Negative")

    verify { callback.onAuthenticationCancelled() }
  }

  @Test
  fun `authenticate calls onAuthenticationError on other error codes`() {
    authCallback.onAuthenticationError(BiometricPrompt.ERROR_LOCKOUT, "Locked out")

    verify { callback.onAuthenticationError(BiometricPrompt.ERROR_LOCKOUT, "Locked out") }
  }

  @Test
  fun `authenticate calls onAuthenticationFailed on failed attempt`() {
    authCallback.onAuthenticationFailed()

    verify { callback.onAuthenticationFailed() }
  }
}
