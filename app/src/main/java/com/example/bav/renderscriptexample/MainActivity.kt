package com.example.bav.renderscriptexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var solver: Solver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        solver = Solver(this)

        kotlin.setOnClickListener {
            val size = editText.text.toString().toInt()
            launch {
                runCalc(size, true)
            }
        }

        renderscript.setOnClickListener {
            val size = editText.text.toString().toInt()
            launch {
               runCalc(size, false)
            }
        }
    }

    private fun runCalc(size: Int, isViaKotlin: Boolean) {
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
            if (isViaKotlin) solver.solveViaKotlin(aSet, bSet)
            else solver.solveViaRenderScript(aSet, bSet)
            end = android.os.SystemClock.uptimeMillis()

            totalTime += end - start
        }
        val msg = "${if (isViaKotlin) "Kotlin" else "RS"} = ${totalTime / STEPS} ms"
        Log.d("MainActivity", msg)
        runOnUiThread { Toast.makeText(this, msg, Toast.LENGTH_LONG).show() }
    }
}
