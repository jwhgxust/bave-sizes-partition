package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.combinatorics

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DPartitionRecorder
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DQuadraticDroppingProbability
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.IDPartitionPattern
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.IDQuadraticPartitionSystem
import java.util.HashMap

class DCombinatoricsPartitionSystem(override val existingIDPartitionSet: MutableSet<DCombinatoricsPartitioner> = mutableSetOf(),
                                    override val randomDataGenerator: RandomDataGenerator = RandomDataGenerator()) :
        IDQuadraticPartitionSystem<DCombinatoricsPartitioner> {
    override fun createPartition(baveLength: Int,
                                 averageDroppingProbability: Double,
                                 droppingUniformity: Double,
                                 minDroppingPosRatio: Double): DCombinatoricsPartitioner {
        return DCombinatoricsPartitioner(baveLength, averageDroppingProbability,
                droppingUniformity, minDroppingPosRatio, randomDataGenerator)
    }

    override fun normalLengthBiasSim(iterations: Int,
                                     baveLengthMean: Double,
                                     baveLengthStd: Double,
                                     bias: Int,
                                     averageDroppingProbability: Double,
                                     droppingUniformity: Double,
                                     minDroppingPosRatio: Double): DPartitionRecorder<IDPartitionPattern> {
        val makeMap = HashMap<Int, DCombinatoricsPartitioner>()
        if (baveLengthMean - bias < 1) {
            throw IllegalArgumentException(String.format(
                    "Expected {baveLengthMean - bias < 1},but get {baveLengthMean - bias = %f}", baveLengthMean - bias))
        }
        existingIDPartitionSet.clear()
        val recorder = DPartitionRecorder<IDPartitionPattern>()
        for (i in 1..iterations) {
            val baveLength = randomDataGenerator.nextNormalLimitedIntBias(baveLengthMean, baveLengthStd, bias)
            val partition: DCombinatoricsPartitioner
            if (makeMap.containsKey(baveLength)) {
                partition = makeMap[baveLength]!!
            } else {
                partition = createPartition(baveLength,
                        averageDroppingProbability, droppingUniformity, minDroppingPosRatio)
                makeMap[baveLength] = partition
                existingIDPartitionSet.add(partition)
            }
            recorder.addPartition(i.toString(), partition.partition())
        }
        return recorder
    }
}


fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val stepwise = DCombinatoricsPartitionSystem()
    val baveLength = 16
    val reelability = 0.8
    val averageDroppingProbability = (1.0 / 0.8 - 1.0) / 15
    val droppingUniformity = 0.5
    val minDroppingPosRatio = 0.3
    val recorder = stepwise.fixLengthSim(1000000,
            baveLength, averageDroppingProbability, droppingUniformity, minDroppingPosRatio)
    val droppingProbability = DQuadraticDroppingProbability(baveLength, averageDroppingProbability, droppingUniformity, minDroppingPosRatio)

    println("模拟获得解舒丝长统计结果：")
    System.out.println(recorder.partLengthFrequency())
    System.out.println("计算值   :" + droppingProbability.nonBrokenLengthProbabilities())
    println("模拟获得的中途落绪位置统计结果：")
    System.out.println(recorder.midwayDroppingPosesFrequency())
    System.out.println("计算值   :" + droppingProbability.droppingsNormalized)

    val recorder2 = stepwise.normalLengthBiasSim(100000,
            baveLength.toDouble(), 3.0, 6, averageDroppingProbability, droppingUniformity, minDroppingPosRatio)
    println(stepwise.existingIDPartitionSet.size)
    println("模拟获得解舒丝长统计结果：")
    System.out.println(recorder2.partLengthFrequency())
    println("模拟获得的中途落绪位置统计结果：")
    System.out.println(recorder2.midwayDroppingPosesFrequency())
    System.out.println("计算值   :" + droppingProbability.droppingsNormalized)

    println("运行时间：" + (System.currentTimeMillis() - start))
}