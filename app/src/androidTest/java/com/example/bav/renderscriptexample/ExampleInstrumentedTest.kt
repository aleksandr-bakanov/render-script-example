package com.example.bav.renderscriptexample

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.bav.renderscriptexample", appContext.packageName)
    }

    @Test
    fun solverTest() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val solver = Solver(appContext)
        val aSet = setOf(1, 2, 4, 5, 6)
        val bSet = setOf(1, 3, 5)
        val rSet = solver.solveViaRenderScript(aSet, bSet)
        assertEquals(2, rSet.size)
        assertTrue(rSet.contains(1))
        assertTrue(rSet.contains(5))
        assertFalse(rSet.contains(3))
    }

    @Test
    fun oddEvenTest() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val solver = Solver(appContext)
        val array = intArrayOf(3, 2, 4, 5, 1)
        val res = solver.oddEvenSort(array)
        Log.d("sort.rs: Test", "res = ${res.joinToString()}")
    }
}
