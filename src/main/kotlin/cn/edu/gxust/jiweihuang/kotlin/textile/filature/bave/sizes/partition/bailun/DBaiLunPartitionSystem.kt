package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.bailun

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.DQuadraticDroppingProbability
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition.IDQuadraticPartitionSystem


class DBaiLunPartitionSystem(override val existingIDPartitionSet: MutableSet<DBailunPartitioner> = hashSetOf(),
                             override val randomDataGenerator: RandomDataGenerator = RandomDataGenerator()) :
        IDQuadraticPartitionSystem<DBailunPartitioner> {

    override fun createPartition(baveLength: Int,
                        averageDroppingProbability: Double,
                        droppingUniformity: Double,
                        minDroppingPosRatio: Double): DBailunPartitioner {
        return DBailunPartitioner(baveLength, averageDroppingProbability, droppingUniformity, minDroppingPosRatio, randomDataGenerator)
    }
}

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()

    val stepwise = DBaiLunPartitionSystem()
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

    val recorder2 = stepwise.normalLengthBiasSim(1000000,
            baveLength.toDouble(), 3.0, 9, averageDroppingProbability, droppingUniformity, minDroppingPosRatio)
    println("模拟获得解舒丝长统计结果：")
    System.out.println(recorder2.partLengthFrequency())
    println("模拟获得解舒丝长统计结果：")
    System.out.println(recorder2.midwayDroppingPosesFrequency())
    System.out.println("计算值   :" + droppingProbability.droppingsNormalized)

    println("运行时间：" + (System.currentTimeMillis() - start))
}