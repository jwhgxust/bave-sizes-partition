package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.combinatorics

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DQuadraticDroppingProbability
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.IDPartitioner
import org.paukov.combinatorics3.Generator
import org.paukov.combinatorics3.SubSetGenerator

class DCombinatoricsPartitioner(
        baveLength: Int,
        averageDroppingProbability: Double,
        droppingUniformity: Double,
        minDroppingPosRatio: Double,
        override val randomDataGenerator: RandomDataGenerator = RandomDataGenerator()
) : DQuadraticDroppingProbability(baveLength, averageDroppingProbability, droppingUniformity, minDroppingPosRatio), IDPartitioner {
    private val subSetGenerator: SubSetGenerator<Int> = Generator.subset(nodeIndexes.toList())
    private val allPartitionPattern = mutableListOf<DCombinatoricsPartitionPattern>()

    init {
        subSetGenerator.simple().stream().forEach { droppingIndexes ->
            var probability = 1.0
            for (i in nodeIndexes) {
                probability *= (if (i in droppingIndexes) dropping(i) else notDropping(i))
            }
            droppingIndexes.add(0,0)
            droppingIndexes.add(baveLength)
            allPartitionPattern.add(DCombinatoricsPartitionPattern(baveLength, droppingIndexes, probability))
        }
        allPartitionPattern.sortDescending()
    }

    override fun partition(): DCombinatoricsPartitionPattern {
        val uniformRandNum = randomDataGenerator.nextDouble()
        var makeProbability = allPartitionPattern[0].probability
        if (uniformRandNum <= makeProbability) {
            return allPartitionPattern[0]
        } else {
            for (i in 1 until allPartitionPattern.size) {
                makeProbability += allPartitionPattern[i].probability
                if (uniformRandNum <= makeProbability) {
                    return allPartitionPattern[i]
                }
            }
        }
        throw RuntimeException("DCombinatoricsPartitioner.partition error:{generated uniformly distributed random Numbers:$uniformRandNum}")
    }
}

fun main() {
    val partitioner = DCombinatoricsPartitioner(
            16,
            (1.0 / 0.8 - 1.0) / 15.0,
            0.5,
            0.3
    )
    val times = 1000000
    for (i in 1..times) {
        val pattern = partitioner.partition()
        System.out.println("droppingPoses = ${pattern.droppingPoses}, nonBrokenLengths = ${pattern.nonBrokenLengths}, probability = ${pattern.probability}.")
    }
}