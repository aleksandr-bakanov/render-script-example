package com.example.bav.renderscriptexample

import android.content.Context
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript

class Solver(private val context: Context,
        private val rs: RenderScript = RenderScript.create(context),
        private val intersectScript: ScriptC_intersect = ScriptC_intersect(rs)) {

    fun solveViaKotlin(aSet: Set<Int>, bSet: Set<Int>): Set<Int> = aSet.intersect(bSet)

    fun solveViaRenderScript(aSet: Set<Int>, bSet: Set<Int>): Set<Int> {
        val aArr = aSet.toIntArray().sortedArray()
        val bArr = bSet.toIntArray()
        val rArr = IntArray(bArr.size, {0})

        val aAllocation = Allocation.createSized(rs, Element.I32(rs), aArr.size)
        val bAllocation = Allocation.createSized(rs, Element.I32(rs), bArr.size)
        val rAllocation = Allocation.createSized(rs, Element.I32(rs), rArr.size)

        aAllocation.copyFrom(aArr)
        bAllocation.copyFrom(bArr)

        intersectScript.bind_aArray(aAllocation)
        intersectScript._aSize = aArr.size

        intersectScript.forEach_execute(bAllocation, rAllocation)

        rAllocation.copyTo(rArr)

        aAllocation.destroy()
        bAllocation.destroy()
        rAllocation.destroy()

        return rArr.filter { it != 0 }.toSet()
    }

    fun batcherOddEvenMergeSort(array: IntArray): IntArray {
        return IntArray(0)
    }

    fun bitonicSort(array: IntArray): IntArray {
        return IntArray(0)
    }
}
