package dev.mango.labs.auth.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class BiometricUtilsTest {

  private lateinit var context: Context
  private lateinit var biometricManager: BiometricManager

  @Before
  fun setUp() {
    context = RuntimeEnvironment.getApplication()
    biometricManager = io.mockk.mockk()
    mockkStatic(BiometricManager::class)
    every { BiometricManager.from(any()) } returns biometricManager
  }

  @After
  fun tearDown() {
    unmockkAll()
  }

  @Test
  fun `isBiometricAvailable returns true when BIOMETRIC_SUCCESS`() {
    every {
      biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
    } returns BiometricManager.BIOMETRIC_SUCCESS

    assertTrue(BiometricUtils.isBiometricAvailable(context))
  }

  @Test
  fun `isBiometricAvailable returns false for other result codes`() {
    every {
      biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
    } returns BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE

    assertFalse(BiometricUtils.isBiometricAvailable(context))
  }

  @Test
  fun `canAuthenticate delegates to BiometricManager`() {
    every {
      biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
    } returns BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED

    val result = BiometricUtils.canAuthenticate(context)

    assertEquals(BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED, result)
    verify { biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) }
  }
}
