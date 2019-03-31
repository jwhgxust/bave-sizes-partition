package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition

import java.lang.Math.pow
import java.util.*

interface IDDroppingProbability {
    val baveLength: Int
    fun dropping(pos: Int): Double
    fun notDropping(pos: Int): Double = 1.0 - dropping(pos)
    val nodeIndexes: IntRange
        get() = 1 until baveLength


    /**
     *
     * The dropping probability of all nodes on bave discretized.
     *
     * If `M==1`,the return `NavigableMap<Integer, Double>` will be empty.
     *
     * @return `NavigableMap<Integer, Double>` as dropping probability of all nodes on bave discretized.
     */
    val droppingProbabilities: Map<Int, Double>
        get() = nodeIndexes.associateWith { dropping(it) }

    /**
     *
     * The non-dropping probability of all nodes on bave discretized.
     *
     * If `M==1`,the return `NavigableMap<Integer, Double>` will be empty.
     *
     * @return `NavigableMap<Integer, Double>` as non-dropping probability of all nodes on bave discretized.
     */
    val notDroppingProbabilities: Map<Int, Double>
        get() = nodeIndexes.associateWith { dropping(it) }

    /**
     *
     * The sum of dropping probability of all nodes on bave discretized.
     *
     * it should be equal to `averageDroppingTimes`.
     *
     * If `M==1`,the return value will be 0.0.
     *
     * @return `double` as sum of dropping probability of all nodes on bave discretized.
     */
    val droppingProbabilitiesSum: Double
        get() = droppingProbabilities.values.sum()


    /**
     *
     * The average of dropping probability of all nodes on bave discretized.
     *
     * it should be equal to `averageDroppingProbability`.
     *
     * @return `double` as average of dropping probability of all nodes on bave discretized.
     * @Throws `RuntimeException` if `M==1`.
     */
    val droppingProbabilitiesAverage: Double
        get() = droppingProbabilities.values.average()


    /**
     *
     * The sum of non-dropping probability of all nodes on bave discretized.
     *
     * If `M==1`,the return value will be 1.0
     *
     * @return `double` as sum of non-dropping probability of all nodes on bave discretized.
     */
    val notDroppingProbabilitiesSum: Double
        get() {
            if (nodeIndexes.count() == 0) {
                return 1.0
            }
            var makeSum = 0.0
            for (probability in notDroppingProbabilities.values) {
                makeSum += probability
            }
            return makeSum
        }

    /**
     * The average of non-dropping probability of all nodes on bave discretized.
     *
     * @return `double` as average of non-dropping probability of all nodes on bave discretized.
     * @Throws `RuntimeException` if `M==1`.
     */
    val notDroppingProbabilitiesAverage: Double
        get() {
            if (nodeIndexes.count() == 0) {
                throw RuntimeException("Because the {@code baveLength == 1},so This method should not be called.")
            }
            return notDroppingProbabilitiesSum / nodeIndexes.count()
        }

    /**
     *
     * The normalization of dropping probability of all nodes on bave discretized.
     *
     * If `M==1`,the return `NavigableMap<Integer, Double>` will be empty.
     *
     * @return `NavigableMap<Integer, Double>` as normalization of dropping probability of all nodes on bave discretized.
     */
    val normalizedDroppingProbabilities: NavigableMap<Int, Double>
        get() {
            val makeMap = TreeMap<Int, Double>()
            val probability = droppingProbabilities
            for (index in 1 until baveLength) {
                makeMap[index] = probability[index]!! / droppingProbabilitiesSum
            }
            return makeMap
        }

    /**
     * The normalization of non-dropping probability of all nodes on bave discretized.
     *
     * If `M==1`,the return `NavigableMap<Integer, Double>` will be empty.
     *
     * @return `NavigableMap<Integer, Double>` as normalization of non-dropping probability of all nodes on bave discretized.
     */
    fun getNormalizedNotDroppingProbabilities(): NavigableMap<Int, Double> {
        val makeMap = TreeMap<Int, Double>()
        val probability = notDroppingProbabilities
        for (index in 1 until baveLength) {
            makeMap[index] = probability[index]!! / notDroppingProbabilitiesSum
        }
        return makeMap
    }

    /**
     * The probability of non-broken filament length at specified length {@param length}.
     *
     * If `M==1`,the return value will be 1.0
     *
     * @param length the length of non-broken filament,required that it in `[1,M]`.
     * @return probability of non-broken filament length at specified length.
     */
    fun nonBrokenLengthProbability(length: Int): Double {
        if (length < 1 || length > baveLength) {
            throw IllegalArgumentException(String.format(
                    "Expected the parameter {length} should be in [1,baveLength],but get {length = %d}", length))
        }
        var make_sum = 0.0
        if (length == baveLength) {
            make_sum = 1.0
            for (i in 1 until baveLength) {
                make_sum *= notDropping(i)
            }
            return make_sum / (1.0 + droppingProbabilitiesSum)
        } else {
            for (i in 1..baveLength - length - 1) {
                var make_part1 = dropping(i) * dropping(i + length)
                for (j in i + 1..i + length - 1) {
                    make_part1 *= notDropping(j)
                }
                make_sum += make_part1
            }
            var make_part2 = dropping(length)
            for (j in 1..length - 1) {
                make_part2 *= notDropping(j)
            }
            make_sum += make_part2
            var make_part3 = dropping(baveLength - length)
            for (j in baveLength - length + 1..baveLength - 1) {
                make_part3 *= notDropping(j)
            }
            make_sum += make_part3
            return make_sum / (1.0 + droppingProbabilitiesSum)
        }
    }

    /**
     * The probabilities of all non-broken filament length that include `[1,M]`.
     *
     * If `M==1`,the return `NavigableMap<Integer, Double>` will be `1->1.0`
     *
     * @return `NavigableMap<Integer, Double>` as probabilities of all non-broken filament length that include `[1,M]`.
     */
    fun nonBrokenLengthProbabilities(): NavigableMap<Int, Double> {
        val makeMap = TreeMap<Int, Double>()
        for (i in 1..baveLength) {
            makeMap[i] = nonBrokenLengthProbability(i)
        }
        return makeMap
    }

    /**
     *
     * The sum of probabilities of all non-broken filament length that in `[1,M]`.
     *
     * it should be equal to `1.0`
     *
     * If `M==1`,the return will also be `1.0`
     *
     * @return sum of probabilities of all non-broken filament length that in `[1,M]`.
     */
    fun nonBrokenLengthProbabilitiesSum(): Double {
        var makeResult = 0.0
        for (probability in nonBrokenLengthProbabilities().values) {
            makeResult += probability
        }
        return makeResult
    }

    fun nonBrokenLengthProbabilityBaiLun(length: Int): Double {
        if (length < 1 || length > baveLength) {
            throw IllegalArgumentException(String.format(
                    "Expected the parameter {length} should be in [1,baveLength],but get {length = %d}", length))
        }
        if (length == 1) {
            var makeSum = 0.0
            for (j in 1..baveLength - 2) {
                makeSum += dropping(j) * dropping(j + 1)
            }
            return (dropping(1) + dropping(baveLength - 1) +
                    makeSum) / (1.0 + droppingProbabilitiesSum)
        } else if (length == baveLength) {
            var makeMul = 1.0
            for (j in 1 until baveLength) {
                makeMul *= notDropping(j)
            }
            return makeMul / (1.0 + droppingProbabilitiesSum)
        } else {
            var makeSum = 0.0
            var makeMul1 = dropping(length)
            for (j in 1..length - 1) {
                makeMul1 *= notDropping(j)
            }
            makeSum += makeMul1
            var makeMul2 = dropping(baveLength - length)
            for (j in 1..length - 1) {
                makeMul2 *= notDropping(baveLength - length + j)
            }
            makeSum += makeMul2
            for (r in 1..baveLength - length - 1) {
                var makeMul3 = dropping(r) * dropping(r + length)
                for (j in 1..length - 1) {
                    makeMul3 *= notDropping(r + j)
                }
                makeSum += makeMul3
            }
            return makeSum / (1.0 + droppingProbabilitiesSum)
        }
    }

    fun nonBrokenLengthProbabilityBaiLun2(length: Int): Double {
        if (length < 1 || length > baveLength) {
            throw IllegalArgumentException(String.format(
                    "Expected the parameter {length} should be in [1,baveLength],but get {length = %d}", length))
        }
        if (length == 1) {
            var makeSum = 0.0
            for (j in 1..baveLength - 2) {
                makeSum += dropping(j) * dropping(j + 1)
            }
            return (dropping(1) + dropping(baveLength - 1) +
                    makeSum) / (1.0 + droppingProbabilitiesSum)
        } else if (length == baveLength) {
            var makeMul = 1.0
            for (j in 1 until baveLength) {
                makeMul *= notDropping(j)
            }
            return makeMul / (1.0 + droppingProbabilitiesSum)
        } else {
            var makeSum = 0.0
            var makeMul1 = dropping(length)
            for (j in 1..length - 1) {
                makeMul1 *= notDropping(j)
            }
            makeSum += makeMul1
            for (r in 0..baveLength - length - 1) {
                var makeMul2 = dropping(r + 1)
                for (j in 2..length) {
                    makeMul2 *= notDropping(r + j)
                }
                makeSum += makeMul2
            }
            for (r in 1..baveLength - length - 1) {
                var makeMul2 = dropping(r)
                for (j in 1..length) {
                    makeMul2 *= notDropping(r + j)
                }
                makeSum -= makeMul2
            }
            return makeSum / (1.0 + droppingProbabilitiesSum)
        }
    }

    fun nonBrokenLengthProbabilitiesBaiLun(): NavigableMap<Int, Double> {
        val makeMap = TreeMap<Int, Double>()
        for (i in 1..baveLength) {
            makeMap[i] = nonBrokenLengthProbabilityBaiLun(i)
        }
        return makeMap
    }

    fun nonBrokenLengthProbabilitiesSumBaiLun(): Double {
        var makeResult = 0.0
        for (probability in nonBrokenLengthProbabilitiesBaiLun().values) {
            makeResult += probability
        }
        return makeResult
    }
}

interface IDQuadraticDroppingProbability : IDDroppingProbability {
    val averageDroppingProbability: Double
    val droppingUniformity: Double
    val minDroppingPosRatio: Double
    //==========================================================
    val minDroppingPos: Double
        get() = minDroppingPosRatio * baveLength
    val minDroppingProbability: Double
        get() = droppingUniformity * averageDroppingProbability
    val averageDroppingTimes: Double
        get() = averageDroppingProbability * (baveLength - 1)
    val reelability: Double
        get() = 1.0 / (1.0 + averageDroppingTimes)

    val modelCoef: Double
        get() = (6.0 * averageDroppingProbability * (1.0 -
                droppingUniformity)) / (2.0 * pow(baveLength.toDouble(),
                2.0) * (1.0 - 3.0 * minDroppingPosRatio +
                3.0 * pow(minDroppingPosRatio, 2.0)) - baveLength)

    override fun dropping(pos: Int): Double {
        if (pos in 1 until baveLength) {
            return modelCoef * pow(pos - minDroppingPos, 2.0) + minDroppingProbability
        } else {
            throw IllegalArgumentException("Expected the parameter {pos in [0.0,$baveLength]},but get {pos = $pos}.")
        }
    }

    //============================================================
    val quadraticVertexA: Double
        get() = modelCoef
    val quadraticVertexB: Double
        get() = minDroppingPos
    val quadraticVertexC: Double
        get() = minDroppingProbability

    fun value(x: Double): Double {
        return quadraticVertexA * pow(x - quadraticVertexB, 2.0) + quadraticVertexC
    }

    //============================================================

    //============================================================

}

open class DQuadraticDroppingProbability(override val baveLength: Int,
                                         override val averageDroppingProbability: Double,
                                         override val droppingUniformity: Double,
                                         override val minDroppingPosRatio: Double) :
        IDQuadraticDroppingProbability {
    init {
        if (baveLength <= 1) {
            throw IllegalArgumentException("Expected the parameter {baveLength >= 2},but get {baveLength = $baveLength}.")
        }
        if ((!averageDroppingProbability.isFinite()) || averageDroppingProbability <= 0.0) {
            throw IllegalArgumentException("Expected the parameter {averageDroppingProbability > 0.0} and is finite,but get {averageDroppingProbability = $averageDroppingProbability}.")
        }
        if ((!droppingUniformity.isFinite()) || droppingUniformity < 0.0 || droppingUniformity > 1.0) {
            throw IllegalArgumentException("Expected the parameter {0.0 <= droppingUniformity <= 1.0} and is finite,but get {droppingUniformity = $droppingUniformity}.")
        }
        if ((!minDroppingPosRatio.isFinite()) || minDroppingPosRatio <= 0.0 || minDroppingPosRatio >= 1.0) {
            throw IllegalArgumentException("Expected the parameter {0.0 < minDroppingPosRatio < 1.0} and is finite,but get {minDroppingPosRatio = $minDroppingPosRatio}.")
        }
    }


    override fun toString(): String {
        return "DQuadraticDroppingProbability(M=$baveLength, beta=$averageDroppingProbability, C=$droppingUniformity, rho=$minDroppingPosRatio)"
    }
}

fun reelabilityToAverageDroppingTimes(reelability: Double): Double {
    return 1.0 / reelability - 1.0
}

fun reelabilityToAverageDroppingProbability(reelability: Double, baveLength: Int): Double {
    return (1.0 / reelability - 1.0) / (baveLength - 1)
}