package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.combinatorics

import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DPartitionPattern

class DCombinatoricsPartitionPattern(
    baveLength: Int,
    droppingPoses: List<Int>,
    val probability: Double
) : DPartitionPattern(baveLength, droppingPoses), Comparable<DCombinatoricsPartitionPattern> {
    override fun compareTo(other: DCombinatoricsPartitionPattern): Int {
        return probability.compareTo(other.probability)
    }
}