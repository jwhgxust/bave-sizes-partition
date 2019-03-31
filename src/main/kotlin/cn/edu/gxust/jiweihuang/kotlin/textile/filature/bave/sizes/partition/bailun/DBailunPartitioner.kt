package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.bailun

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DQuadraticDroppingProbability
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.IDPartitioner


class DBailunPartitioner(
        baveLength: Int,
        averageDroppingProbability: Double,
        droppingUniformity: Double,
        minDroppingPosRatio: Double,
        override val randomDataGenerator: RandomDataGenerator = RandomDataGenerator()
) :DQuadraticDroppingProbability(baveLength, averageDroppingProbability, droppingUniformity, minDroppingPosRatio), IDPartitioner {

    private fun genStartPos(): Int {
        val uniformRandNum = randomDataGenerator.nextDouble() * (1.0 + averageDroppingTimes)
        var makeSum = 1.0
        if (uniformRandNum <= makeSum) {
            return 0
        } else {
            for (i in 1 until baveLength) {
                makeSum += dropping(i)
                if (uniformRandNum <= makeSum) {
                    return i
                }
            }
        }
        throw RuntimeException("DBailunPartitioner.genStartPos() error:{generated uniformly distributed random Numbers:$uniformRandNum}.")
    }

    override fun partition(): DBailunPartitionPattern {
        val startPos = genStartPos()
        var uniformRandNum = randomDataGenerator.nextDouble()
        for (i in 1..baveLength - startPos) {
            val makeDroppingProbability: Double = if (startPos + i == baveLength) 1.0 else dropping(startPos + i)
            if (uniformRandNum <= makeDroppingProbability) {
                return DBailunPartitionPattern(baveLength, startPos, startPos + i)
            } else {
                uniformRandNum = randomDataGenerator.nextDouble()
            }
        }
        throw RuntimeException("DBailunPartitioner.partition() error:{generated uniformly distributed random Numbers:$uniformRandNum} and the {startPos = $startPos}.")
    }
}

fun main() {
    val partitioner = DBailunPartitioner(
            16,
            (1.0 / 0.8 - 1.0) / 15.0,
            0.5,
            0.3
    )
    val times = 1000000
    for (i in 1..times) {
        val pattern = partitioner.partition()
        System.out.println("droppingPoses = ${pattern.droppingPoses}, nonBrokenLengths = ${pattern.nonBrokenLengths}.")
    }
}