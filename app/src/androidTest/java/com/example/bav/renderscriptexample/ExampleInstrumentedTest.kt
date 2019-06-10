package com.example.bav.renderscriptexample

import android.os.SystemClock
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import android.util.Log

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.Random
import kotlin.system.measureTimeMillis

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

    @Test
    fun measurementRs() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val solver = Solver(appContext)
        val random = Random(SystemClock.uptimeMillis())

        val aSet = mutableSetOf<Int>()
        val bSet = mutableSetOf<Int>()

        val steps = 5
        var count = 1000

        for (m in 0 until 4) {
            var totalTime = 0L
            for (step in 0 until steps) {
                aSet.clear()
                bSet.clear()
                for (i in 0 until count) {
                    aSet.add(random.nextInt())
                    bSet.add(random.nextInt())
                }
                totalTime += measureTimeMillis { solver.solveViaRenderScript(aSet, bSet) }
            }
            Log.d("intersect.rs     RS", "count = $count, avg = ${totalTime / steps} ms")
            count *= 10
        }
    }

    @Test
    fun measurementKotlin() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val solver = Solver(appContext)
        val random = Random(SystemClock.uptimeMillis())

        val aSet = mutableSetOf<Int>()
        val bSet = mutableSetOf<Int>()

        val steps = 5
        var count = 1000

        for (m in 0 until 4) {
            var totalTime = 0L
            for (step in 0 until steps) {
                aSet.clear()
                bSet.clear()
                for (i in 0 until count) {
                    aSet.add(random.nextInt())
                    bSet.add(random.nextInt())
                }
                totalTime += measureTimeMillis { solver.solveViaKotlin(aSet, bSet) }
            }
            Log.d("intersect.rs Kotlin", "count = $count, avg = ${totalTime / steps} ms")

            count *= 10
        }
    }

    @Test
    fun measurementSortKotlin() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val solver = Solver(appContext)
        val random = Random(SystemClock.uptimeMillis())

        val aSet = mutableSetOf<Int>()

        val steps = 3
        var count = 100

        for (m in 0 until 3) {
            var totalTime = 0L
            for (step in 0 until steps) {
                aSet.clear()
                for (i in 0 until count) {
                    aSet.add(random.nextInt())
                }
                totalTime += measureTimeMillis { solver.sortViaKotlin(aSet.toIntArray()) }
            }
            Log.d("sort.rs Kotlin", "count = $count, avg = ${totalTime / steps} ms")

            count *= 10
        }
    }

    @Test
    fun measurementSortRs() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val solver = Solver(appContext)
        val random = Random(SystemClock.uptimeMillis())

        val aSet = mutableSetOf<Int>()

        val steps = 3
        var count = 100

        for (m in 0 until 3) {
            var totalTime = 0L
            for (step in 0 until steps) {
                aSet.clear()
                for (i in 0 until count) {
                    aSet.add(random.nextInt())
                }
                totalTime += measureTimeMillis { solver.oddEvenSort(aSet.toIntArray()) }
            }
            Log.d("sort.rs     RS", "count = $count, avg = ${totalTime / steps} ms")
            count *= 10
        }
    }
}
