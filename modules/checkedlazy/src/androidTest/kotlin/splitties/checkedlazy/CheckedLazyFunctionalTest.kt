/*
 * Copyright 2019 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */

package splitties.checkedlazy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(RobolectricTestRunner::class)
class CheckedLazyFunctionalTest {

    @Test
    fun testMainThreadLazyBehavior() {
        var refVariable = 0
        val localUnderTest by mainThreadLazy { ++refVariable }
        assertEquals(0, refVariable)
        runBlocking(Dispatchers.Default) {
            assertFailsWith<IllegalStateException> {
                localUnderTest + 0
            }
        }
        assertEquals(0, refVariable)
        runBlocking(Dispatchers.Main) {
            assertEquals(1, localUnderTest)
            assertEquals(1, localUnderTest)
            assertEquals(1, localUnderTest)
        }
        assertEquals(1, refVariable)
        runBlocking(Dispatchers.Default) {
            assertFailsWith<IllegalStateException> {
                localUnderTest + 0
            }
        }
        assertEquals(1, refVariable)
    }
}
