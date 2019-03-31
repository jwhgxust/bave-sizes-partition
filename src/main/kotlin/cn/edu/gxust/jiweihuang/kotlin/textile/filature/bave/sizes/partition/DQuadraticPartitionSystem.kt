package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition

import cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution.PickedNormalIntegerDistribution
import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator
import java.util.NavigableMap
import java.util.TreeMap

import java.lang.Math.round

interface IDQuadraticPartitionSystem<T : IDPartitioner> {

    val existingIDPartitionSet: MutableSet<T>

    val randomDataGenerator: RandomDataGenerator

    fun createPartition(baveLength: Int,
                        averageDroppingProbability: Double,
                        droppingUniformity: Double,
                        minDroppingPosRatio: Double): T

    fun fixLengthSim(iterations: Int,
                     baveLength: Int,
                     averageDroppingProbability: Double,
                     droppingUniformity: Double,
                     minDroppingPosRatio: Double): DPartitionRecorder<IDPartitionPattern> {
        existingIDPartitionSet.clear()
        val recorder = DPartitionRecorder<IDPartitionPattern>()
        val partition = createPartition(baveLength, averageDroppingProbability,
                droppingUniformity, minDroppingPosRatio)
        for (i in 1..iterations) {
            recorder.addPartition(i.toString(), partition.partition())
        }
        existingIDPartitionSet.add(partition)
        return recorder
    }

    fun normalLengthBiasSim(iterations: Int,
                            baveLengthMean: Double,
                            baveLengthStd: Double,
                            bias: Int,
                            averageDroppingProbability: Double,
                            droppingUniformity: Double,
                            minDroppingPosRatio: Double): DPartitionRecorder<IDPartitionPattern> {
        if (baveLengthMean - bias < 1) {
            throw IllegalArgumentException(String.format(
                    "Expected {baveLengthMean - bias < 1},but get {baveLengthMean - bias = %f}", baveLengthMean - bias))
        }
        existingIDPartitionSet.clear()
        val recorder = DPartitionRecorder<IDPartitionPattern>()
        for (i in 1..iterations) {
            val partition = createPartition(randomDataGenerator.nextNormalLimitedIntBias(baveLengthMean, baveLengthStd, bias),
                    averageDroppingProbability, droppingUniformity, minDroppingPosRatio)
            recorder.addPartition(i.toString(), partition.partition())
            existingIDPartitionSet.add(partition)
        }
        return recorder
    }

    fun normalLengthMagnifiedSim(iterations: Int,
                                 baveLengthMean: Double,
                                 baveLengthStd: Double,
                                 magnification: Double,
                                 averageDroppingProbability: Double,
                                 droppingUniformity: Double,
                                 minDroppingPosRatio: Double): DPartitionRecorder<IDPartitionPattern> {
        if (baveLengthMean - magnification * baveLengthMean < 1) {
            throw IllegalArgumentException(String.format(
                    "Expected {baveLengthMean - magnification * baveLengthMean < 1}，but get {baveLengthMean(=%f) - magnification(=%f) * baveLengthMean(=%f) = %f}", baveLengthMean, magnification, baveLengthMean, baveLengthMean - magnification * baveLengthMean))
        }

        existingIDPartitionSet.clear()
        val recorder = DPartitionRecorder<IDPartitionPattern>()
        for (i in 1..iterations) {
            val partition = createPartition(randomDataGenerator.nextNormalLimitedIntScale(baveLengthMean,
                    baveLengthStd, magnification),
                    averageDroppingProbability, droppingUniformity, minDroppingPosRatio)
            recorder.addPartition(i.toString(),
                    partition.partition())
            existingIDPartitionSet.add(partition)
        }
        return recorder
    }


    fun normalBaveLengthBiasNonBrokenLengthProbability(length: Int,
                                                       baveLengthMean: Double,
                                                       baveLengthSD: Double,
                                                       bias: Int,
                                                       averageDroppingProbability: Double,
                                                       droppingUniformity: Double,
                                                       minDroppingPosRatio: Double): Double {
        val lower = round(baveLengthMean).toInt() - bias
        val upper = round(baveLengthMean).toInt() + bias
        val makelower: Int
        if (lower <= length + 1) {
            makelower = length + 1
        } else {
            makelower = lower
        }
        val normalInt = PickedNormalIntegerDistribution(baveLengthMean, baveLengthSD)
        var makeSum = 0.0
        for (M in makelower..upper) {
            val dp = DQuadraticDroppingProbability(M, averageDroppingProbability,
                    droppingUniformity, minDroppingPosRatio)
            makeSum += dp.nonBrokenLengthProbability(length) * normalInt.probability(M)
        }
        val dp2 = DQuadraticDroppingProbability(length, averageDroppingProbability,
                droppingUniformity, minDroppingPosRatio)
        return makeSum + dp2.nonBrokenLengthProbability(length) * normalInt.probability(length)
    }

    fun normalBaveLengthBiasNonBrokenLengthProbabilities(baveLengthMean: Double,
                                                         baveLengthSD: Double,
                                                         bias: Int,
                                                         averageDroppingProbability: Double,
                                                         droppingUniformity: Double,
                                                         minDroppingPosRatio: Double): NavigableMap<Int, Double> {
        val makeMap = TreeMap<Int, Double>()
        val upper = round(baveLengthMean).toInt() + bias
        for (i in 1..upper) {
            makeMap[i] = normalBaveLengthBiasNonBrokenLengthProbability(i, baveLengthMean,
                    baveLengthSD,
                    bias,
                    averageDroppingProbability,
                    droppingUniformity,
                    minDroppingPosRatio)
        }
        return makeMap
    }
}

