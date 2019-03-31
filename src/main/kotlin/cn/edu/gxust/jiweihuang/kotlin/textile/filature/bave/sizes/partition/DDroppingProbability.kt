package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.sizes.partition

import java.lang.Math.pow
import java.util.*

interface IDDroppingProbability {

    /**
     * The length of bave.
     *
     * Symbol: M
     */
    val baveLength: Int

    //=======================================================================

    /**
     * The droppingsAverage dropping probability.
     *
     * Symbol: beta
     */
    val averageDroppingProbability: Double

    /**
     * The dropping uniformity.
     *
     * Sysmbol: C
     */
    val droppingUniformity: Double

    /**
     * The ratio of the minimum dropping probability position to bave length.
     *
     * Symbol: rho
     */
    val minDroppingPosRatio: Double
    //=======================================================================

    /**
     * The position of minimum dropping probability on bave discretized.
     *
     * Symbol: a
     */
    val minDroppingPos: Double
        get() = minDroppingPosRatio * baveLength

    /**
     * The minimum dropping probability on bave discretized.
     *
     * Symbol: b
     */
    val minDroppingProbability: Double
        get() = droppingUniformity * averageDroppingProbability

    /**
     * The droppingsAverage dropping times of bave discretized.
     *
     * Symbol: Z_M
     */
    val averageDroppingTimes: Double
        get() = averageDroppingProbability * (baveLength - 1)

    /**
     * The reelability of bave discretized.
     *
     * Symbol: J_M
     */
    val reelability: Double
        get() = 1.0 / (1.0 + averageDroppingTimes)
    //=======================================================================
    /**
     * The dropping probability at specified `pos`
     *
     * Symbol: p(M)
     */
    fun dropping(pos: Int): Double

    /**
     * The non-dropping probability at specified `pos`
     *
     * Symbol: q(M)
     */
    fun notDropping(pos: Int): Double = 1.0 - dropping(pos)

    //=======================================================================

    /**
     * The indexes of dropping node.
     *
     * Symbol: i
     */
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
    val droppings: Map<Int, Double>
        get() = nodeIndexes.associateWith { dropping(it) }

    /**
     *
     * The non-dropping probability of all nodes on bave discretized.
     *
     * If `M==1`,the return `NavigableMap<Integer, Double>` will be empty.
     *
     * @return `NavigableMap<Integer, Double>` as non-dropping probability of all nodes on bave discretized.
     */
    val notDroppings: Map<Int, Double>
        get() = nodeIndexes.associateWith { dropping(it) }

    /**
     *
     * The sum of dropping probability of all nodes on bave discretized.
     *
     * it should be equal to `averageDroppingTimes`.
     *
     * If `M==1`,the return value will be 0.0.
     *
     * @return `double` as droppingsSum of dropping probability of all nodes on bave discretized.
     */
    val droppingsSum: Double
        get() = droppings.values.sum()


    /**
     *
     * The average of dropping probability of all nodes on bave discretized.
     *
     * it should be equal to `averageDroppingProbability`.
     *
     * @return `double` as droppingsAverage of dropping probability of all nodes on bave discretized.
     * @Throws `RuntimeException` if `M==1`.
     */
    val droppingsAverage: Double
        get() = droppings.values.average()


    /**
     *
     * The sum of non-dropping probability of all nodes on bave discretized.
     *
     * If `M==1`,the return value will be 1.0
     *
     * @return `double` as droppingsSum of non-dropping probability of all nodes on bave discretized.
     */
    val notDroppingsSum: Double
        get() {
            if (nodeIndexes.count() == 0) {
                return 1.0
            }
            var makeSum = 0.0
            for (probability in notDroppings.values) {
                makeSum += probability
            }
            return makeSum
        }

    /**
     * The average of non-dropping probability of all nodes on bave discretized.
     *
     * @return `double` as droppingsAverage of non-dropping probability of all nodes on bave discretized.
     * @Throws `RuntimeException` if `M==1`.
     */
    val notDroppingsAverage: Double
        get() {
            if (nodeIndexes.count() == 0) {
                throw RuntimeException("Because the {@code baveLength == 1},so This method should not be called.")
            }
            return notDroppingsSum / nodeIndexes.count()
        }

    /**
     *
     * The normalization of dropping probability of all nodes on bave discretized.
     *
     * If `M==1`,the return `NavigableMap<Integer, Double>` will be empty.
     *
     * @return `NavigableMap<Integer, Double>` as normalization of dropping probability of all nodes on bave discretized.
     */
    val droppingsNormalized: NavigableMap<Int, Double>
        get() {
            val makeMap = TreeMap<Int, Double>()
            for (index in 1 until baveLength) {
                makeMap[index] = droppings.getValue(index) / droppingsSum
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
    fun notDroppingsNormalized(): NavigableMap<Int, Double> {
        val makeMap = TreeMap<Int, Double>()
        for (index in 1 until baveLength) {
            makeMap[index] = notDroppings.getValue(index) / notDroppingsSum
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
        var makeSum = 0.0
        if (length == baveLength) {
            makeSum = 1.0
            for (i in 1 until baveLength) {
                makeSum *= notDropping(i)
            }
            return makeSum / (1.0 + droppingsSum)
        } else {
            for (i in 1 until baveLength - length) {
                var makePart1 = dropping(i) * dropping(i + length)
                for (j in (i + 1)..(i + length - 1)) {
                    makePart1 *= notDropping(j)
                }
                makeSum += makePart1
            }
            var makePart2 = dropping(length)
            for (j in 1 until length) {
                makePart2 *= notDropping(j)
            }
            makeSum += makePart2
            var makePart3 = dropping(baveLength - length)
            for (j in (baveLength - length + 1)..(baveLength - 1)) {
                makePart3 *= notDropping(j)
            }
            makeSum += makePart3
            return makeSum / (1.0 + droppingsSum)
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
     * The droppingsSum of probabilities of all non-broken filament length that in `[1,M]`.
     *
     * it should be equal to `1.0`
     *
     * If `M==1`,the return will also be `1.0`
     *
     * @return droppingsSum of probabilities of all non-broken filament length that in `[1,M]`.
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
        when (length) {
            1 -> {
                var makeSum = 0.0
                for (j in 1..baveLength - 2) {
                    makeSum += dropping(j) * dropping(j + 1)
                }
                return (dropping(1) + dropping(baveLength - 1) +
                        makeSum) / (1.0 + droppingsSum)
            }
            baveLength -> {
                var makeMul = 1.0
                for (j in 1 until baveLength) {
                    makeMul *= notDropping(j)
                }
                return makeMul / (1.0 + droppingsSum)
            }
            else -> {
                var makeSum = 0.0
                var makeMul1 = dropping(length)
                for (j in 1 until length) {
                    makeMul1 *= notDropping(j)
                }
                makeSum += makeMul1
                var makeMul2 = dropping(baveLength - length)
                for (j in 1 until length) {
                    makeMul2 *= notDropping(baveLength - length + j)
                }
                makeSum += makeMul2
                for (r in 1 until (baveLength - length)) {
                    var makeMul3 = dropping(r) * dropping(r + length)
                    for (j in 1 until length) {
                        makeMul3 *= notDropping(r + j)
                    }
                    makeSum += makeMul3
                }
                return makeSum / (1.0 + droppingsSum)
            }
        }
    }

    fun nonBrokenLengthProbabilityBaiLun2(length: Int): Double {
        if (length < 1 || length > baveLength) {
            throw IllegalArgumentException(String.format(
                    "Expected the parameter {length} should be in [1,baveLength],but get {length = %d}", length))
        }
        when (length) {
            1 -> {
                var makeSum = 0.0
                for (j in 1..baveLength - 2) {
                    makeSum += dropping(j) * dropping(j + 1)
                }
                return (dropping(1) + dropping(baveLength - 1) +
                        makeSum) / (1.0 + droppingsSum)
            }
            baveLength -> {
                var makeMul = 1.0
                for (j in 1 until baveLength) {
                    makeMul *= notDropping(j)
                }
                return makeMul / (1.0 + droppingsSum)
            }
            else -> {
                var makeSum = 0.0
                var makeMul1 = dropping(length)
                for (j in 1 until length) {
                    makeMul1 *= notDropping(j)
                }
                makeSum += makeMul1
                for (r in 0 until (baveLength - length)) {
                    var makeMul2 = dropping(r + 1)
                    for (j in 2..length) {
                        makeMul2 *= notDropping(r + j)
                    }
                    makeSum += makeMul2
                }
                for (r in 1 until (baveLength - length)) {
                    var makeMul2 = dropping(r)
                    for (j in 1..length) {
                        makeMul2 *= notDropping(r + j)
                    }
                    makeSum -= makeMul2
                }
                return makeSum / (1.0 + droppingsSum)
            }
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

    //==========================================================
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
}

open class DQuadraticDroppingProbability(final override val baveLength: Int,
                                         final override val averageDroppingProbability: Double,
                                         final override val droppingUniformity: Double,
                                         final override val minDroppingPosRatio: Double) :
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