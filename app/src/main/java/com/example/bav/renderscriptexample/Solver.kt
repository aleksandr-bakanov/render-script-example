package com.example.bav.renderscriptexample

import android.content.Context
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript

class Solver(private val context: Context) {

  fun solveViaKotlin(aSet: Set<Int>, bSet: Set<Int>): Set<Int> = aSet.intersect(bSet)

  fun solveViaRenderScript(aSet: Set<Int>, bSet: Set<Int>): Set<Int> {
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

    aAllocation.destroy()
    bAllocation.destroy()
    rAllocation.destroy()
    intersectScript.destroy()

    return rArr.filter { it != 0 }.toSet()
  }
}