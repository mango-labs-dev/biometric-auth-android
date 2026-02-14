package dev.mango.labs.auth.biometric

import androidx.fragment.app.FragmentActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BiometricAuthManagerBuilderTest {

  private lateinit var activity: FragmentActivity

  @Before
  fun setUp() {
    activity = Robolectric.buildActivity(FragmentActivity::class.java)
      .create()
      .get()
  }

  @Test
  fun `build with title and negativeButtonText succeeds`() {
    val manager = BiometricAuthManager.Builder(activity)
      .setTitle("Authenticate")
      .setNegativeButtonText("Cancel")
      .build()

    assertNotNull(manager)
  }

  @Test(expected = IllegalStateException::class)
  fun `build without title throws IllegalStateException`() {
    BiometricAuthManager.Builder(activity)
      .setNegativeButtonText("Cancel")
      .build()
  }

  @Test(expected = IllegalStateException::class)
  fun `build without negativeButtonText and no device credential throws IllegalStateException`() {
    BiometricAuthManager.Builder(activity)
      .setTitle("Authenticate")
      .build()
  }

  @Test
  fun `build with allowDeviceCredential skips negativeButtonText requirement`() {
    val manager = BiometricAuthManager.Builder(activity)
      .setTitle("Authenticate")
      .setAllowDeviceCredential(true)
      .build()

    assertNotNull(manager)
  }

  @Test
  fun `builder setters return builder instance for chaining`() {
    val builder = BiometricAuthManager.Builder(activity)

    val result1 = builder.setTitle("Title")
    val result2 = builder.setSubtitle("Subtitle")
    val result3 = builder.setDescription("Description")
    val result4 = builder.setNegativeButtonText("Cancel")
    val result5 = builder.setAllowDeviceCredential(true)

    assertEquals(builder, result1)
    assertEquals(builder, result2)
    assertEquals(builder, result3)
    assertEquals(builder, result4)
    assertEquals(builder, result5)
  }

  @Test
  fun `builder stores all provided values correctly`() {
    val builder = BiometricAuthManager.Builder(activity)
      .setTitle("Test Title")
      .setSubtitle("Test Subtitle")
      .setDescription("Test Description")
      .setNegativeButtonText("Cancel")
      .setAllowDeviceCredential(true)

    assertEquals("Test Title", builder.title)
    assertEquals("Test Subtitle", builder.subtitle)
    assertEquals("Test Description", builder.description)
    assertEquals("Cancel", builder.negativeButtonText)
    assertTrue(builder.allowDeviceCredential)
  }
}
