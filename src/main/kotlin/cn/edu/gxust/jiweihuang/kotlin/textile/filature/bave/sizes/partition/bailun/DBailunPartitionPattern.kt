package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.bailun

import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DPartitionPattern

class DBailunPartitionPattern(
    baveLength: Int,
    val startPos: Int,
    val endPos: Int
) : DPartitionPattern(baveLength, listOf(startPos, endPos)) {
    val nonBrokenLength: Int
        get() = endPos - startPos
    override val nonBrokenLengths: List<Int>
        get() = listOf(nonBrokenLength)
    override val droppingTimes: Int
        get() {
            return when {
                startPos == 0 && endPos == baveLength -> 0
                startPos == 0 && endPos != baveLength -> 1
                startPos != 0 && endPos == baveLength -> 1
                else -> 2
            }
        }
}