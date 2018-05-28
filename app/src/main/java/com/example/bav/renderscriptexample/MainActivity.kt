package com.example.bav.renderscriptexample

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch
import java.util.*

fun intersectKotlin(aSet: Set<Int>, bSet: Set<Int>): Set<Int> = aSet.intersect(bSet)

fun intersectRs(aSet: Set<Int>, bSet: Set<Int>): Set<Int> {
    val aArr = aSet.toIntArray().sortedArray()
    val bArr = bSet.toIntArray()
    val rArr = IntArray(bArr.size, {0})

    val rs = RenderScript.create(context)

    val aAllocation = Allocation.createSized(rs, Element.I32(rs), aArr.size)
    val bAllocation = Allocation.createSized(rs, Element.I32(rs), bArr.size)
    val rAllocation = Allocation.createSized(rs, Element.I32(rs), rArr.size)

    aAllocation.copyFrom(aArr)
    bAllocation.copyFrom(bArr)

    val intersectScript = ScriptC_intersect(rs)
    intersectScript.bind_aArray(aAllocation)
    intersectScript._aSize = aArr.size

    intersectScript.forEach_execute(bAllocation, rAllocation)

    rAllocation.copyTo(rArr)
    return rArr.filter { it != 0 }.toSet()
}

lateinit var context: Context

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        button.setOnClickListener {
            val size = editText.text.toString().toInt()
            /*launch {
                runCalc(size)
            }*/
            val task = MyTask()
            task.execute(size)
        }

    }

    class MyTask : AsyncTask<Int, Unit, Unit>() {
        override fun doInBackground(vararg p0: Int?) {
            val size = p0[0]
            if (size != null) {
                val aSet = mutableSetOf<Int>()
                val bSet = mutableSetOf<Int>()

                val STEPS = 5
                val random = Random(android.os.SystemClock.uptimeMillis())

                var totalTime = 0L
                var start = 0L
                var end = 0L

                for (step in 0 until STEPS) {
                    aSet.clear()
                    bSet.clear()
                    for (i in 0 until size) {
                        aSet.add(random.nextInt())
                        bSet.add(random.nextInt())
                    }

                    start = android.os.SystemClock.uptimeMillis()
                    intersectKotlin(aSet, bSet)
                    end = android.os.SystemClock.uptimeMillis()

                    totalTime += end - start
                }
                Log.d("MainActivity", "Kotlin = ${totalTime / STEPS} ms")

                totalTime = 0
                for (step in 0 until STEPS) {
                    aSet.clear()
                    bSet.clear()
                    for (i in 0 until size) {
                        aSet.add(random.nextInt())
                        bSet.add(random.nextInt())
                    }

                    start = android.os.SystemClock.uptimeMillis()
                    intersectRs(aSet, bSet)
                    end = android.os.SystemClock.uptimeMillis()

                    totalTime += end - start
                }
                Log.d("MainActivity", "Rs = ${totalTime / STEPS} ms")
            }
        }
    }

    private fun runCalc(size: Int) {
        val aSet = mutableSetOf<Int>()
        val bSet = mutableSetOf<Int>()

        val STEPS = 5
        val random = Random(android.os.SystemClock.uptimeMillis())

        var totalTime = 0L
        var start = 0L
        var end = 0L

        for (step in 0 until STEPS) {
            aSet.clear()
            bSet.clear()
            for (i in 0 until size) {
                aSet.add(random.nextInt())
                bSet.add(random.nextInt())
            }

            start = android.os.SystemClock.uptimeMillis()
            intersectKotlin(aSet, bSet)
            end = android.os.SystemClock.uptimeMillis()

            totalTime += end - start
        }
        Log.d("MainActivity", "Kotlin = ${totalTime / STEPS} ms")

        totalTime = 0
        for (step in 0 until STEPS) {
            aSet.clear()
            bSet.clear()
            for (i in 0 until size) {
                aSet.add(random.nextInt())
                bSet.add(random.nextInt())
            }

            start = android.os.SystemClock.uptimeMillis()
            intersectRs(aSet, bSet)
            end = android.os.SystemClock.uptimeMillis()

            totalTime += end - start
        }
        Log.d("MainActivity", "Rs = ${totalTime / STEPS} ms")
    }

    private fun intersectKotlin(aSet: Set<Int>, bSet: Set<Int>): Set<Int> = aSet.intersect(bSet)

    private fun intersectRs(aSet: Set<Int>, bSet: Set<Int>): Set<Int> {
        val aArr = aSet.toIntArray().sortedArray()
        val bArr = bSet.toIntArray()
        val rArr = IntArray(bArr.size, {0})

        val rs = RenderScript.create(this)

        val aAllocation = Allocation.createSized(rs, Element.I32(rs), aArr.size)
        val bAllocation = Allocation.createSized(rs, Element.I32(rs), bArr.size)
        val rAllocation = Allocation.createSized(rs, Element.I32(rs), rArr.size)

        aAllocation.copyFrom(aArr)
        bAllocation.copyFrom(bArr)

        val intersectScript = ScriptC_intersect(rs)
        intersectScript.bind_aArray(aAllocation)
        intersectScript._aSize = aArr.size

        intersectScript.forEach_execute(bAllocation, rAllocation)

        rAllocation.copyTo(rArr)
        return rArr.filter { it != 0 }.toSet()
    }
}
