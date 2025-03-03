@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.resource.image

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.katan.yoki.createTestYoki
import kotlin.test.Test
import kotlin.test.fail

class RemoveImageIT {

    @Test
    fun `remove image`() = runTest {
        val client = createTestYoki()
        val image = "busybox:latest"

        try {
            client.images.pull(image).collect()
        } catch (e: Throwable) {
            fail("Failed to pull image", e)
        }

        client.images.remove(image)
    }
}
