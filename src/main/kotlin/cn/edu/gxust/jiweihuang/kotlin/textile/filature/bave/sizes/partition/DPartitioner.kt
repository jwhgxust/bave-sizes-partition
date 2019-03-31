package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator

interface IDPartitioner{
    val randomDataGenerator: RandomDataGenerator
    fun partition(): IDPartitionPattern
}