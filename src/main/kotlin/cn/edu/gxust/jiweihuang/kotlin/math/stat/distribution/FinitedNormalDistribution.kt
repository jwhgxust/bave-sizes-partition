package cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution

import java.lang.RuntimeException
import kotlin.math.pow


class FinitedNormalDistribution(mean: Double, val halfLimit: Double,
                                val order: Int = 3,
                                solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) :
        RestrictedNormalDistribution(mean, halfLimit.pow(2) / (2.0 * order + 3.0),
                mean - halfLimit, mean + halfLimit, solverAbsoluteAccuracy) {
    init {
        if ((!halfLimit.isFinite()) || halfLimit <= 0) {
            throw IllegalArgumentException("Expected the parameter {initHalfLimit > 0} and is finite,but get {initHalfLimit = $halfLimit}.")
        }
        if (order > 10 || order < 0) {
            throw IllegalArgumentException("Expected the parameter {0 <= order <= 10},but get {order = $order}")
        }
    }

    private val doubleFactorial: IntArray = intArrayOf(1, 1, 2, 3, 8, 15, 48, 105, 384, 945, 3840, 10395)

    private val factorial: IntArray = intArrayOf(1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800)

    override fun density(x: Double): Double {
        return (((1.0 - ((x - mean) / halfLimit).pow(2)).pow(order)) / halfLimit) *
                ((doubleFactorial[2 * order + 1] / factorial[order]) / ((2.0).pow(order + 1)))
    }

    override fun cumulativeProbability(x: Double): Double {
        val tem = -((mean - x) * ((halfLimit + mean - x).pow(order)) * ((halfLimit - mean + x).pow(order)) / (halfLimit.pow(2 * order + 1)))
        return when (order) {
            0 -> (halfLimit - mean + x) / (2.0 * halfLimit)
            1 -> (1.0 / 4.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            2 -> (3.0 / 16.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            3 -> (5.0 / 32.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            4 -> (35.0 / 256.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            5 -> (63.0 / 512.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            6 -> (231.0 / 2048.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            7 -> (429.0 / 4096.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            8 -> (6435.0 / 65536.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            9 -> (12155.0 / 131072.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            10 -> (46189.0 / 524288.0) * tem + FinitedNormalDistribution(mean, halfLimit, order - 1).cumulativeProbability(x)
            else -> throw RuntimeException("Nonsupport {FinitedNormalDistribution} when {order > 10}.")
        }
    }

    override fun getNumericalMean(): Double {
        return mean
    }

    override fun getNumericalVariance(): Double {
        return (halfLimit.pow(2)) / (2.0 * order + 3.0)
    }

}