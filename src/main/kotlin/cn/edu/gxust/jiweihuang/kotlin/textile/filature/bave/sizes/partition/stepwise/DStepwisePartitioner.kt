package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.stepwise

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DPartitionPattern
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DQuadraticDroppingProbability
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.IDPartitioner

class DStepwisePartitioner(
        baveLength: Int,
        averageDroppingProbability: Double,
        droppingUniformity: Double,
        minDroppingPosRatio: Double,
        override val randomDataGenerator: RandomDataGenerator = RandomDataGenerator()
) : DQuadraticDroppingProbability(baveLength, averageDroppingProbability, droppingUniformity, minDroppingPosRatio), IDPartitioner {

    /**
     * The core algorithm of stepwise partition for bave discretized.
     *
     * @param startPos the start position index.
     * @return the end position index
     */
    private fun nextStep(startPos: Int): Int {
        if (startPos >= baveLength || startPos < 0) {
            throw RuntimeException("Expected the parameter {0 <= startPos < $baveLength},but get {startPos = $startPos}.")
        }
        if (startPos == baveLength - 1) {
            return baveLength
        }
        val uniformRandNum = randomDataGenerator.nextDouble()
        var makeNotDroppingProbability = 1.0
        for (j in startPos + 1 until baveLength) {
            makeNotDroppingProbability *= notDropping(j)
        }
        if (uniformRandNum <= makeNotDroppingProbability) {
            return baveLength
        }
        var makeSum = makeNotDroppingProbability
        for (t in 1 until baveLength - startPos) {
            var makeMul = 1.0
            for (j in startPos + 1 until startPos + t) {
                makeMul *= notDropping(j)
            }
            makeSum += dropping(startPos + t) * makeMul
            if (uniformRandNum <= makeSum) {
                return startPos + t
            }
        }
        throw RuntimeException("DStepwisePartitioner.nextStep($startPos) Error:{generated uniformly distributed random Numbers:$uniformRandNum}.")
    }

    override fun partition(): DPartitionPattern {
        var startPos = 0
        val makeDroppingPoses = mutableListOf<Int>()
        makeDroppingPoses.add(startPos)
        do {
            val nextInt = nextStep(startPos)
            makeDroppingPoses.add(nextInt)
            startPos = nextInt
        } while (startPos < baveLength)
        return DPartitionPattern(baveLength, makeDroppingPoses)
    }
}

fun main() {
    val partitioner = DStepwisePartitioner(
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