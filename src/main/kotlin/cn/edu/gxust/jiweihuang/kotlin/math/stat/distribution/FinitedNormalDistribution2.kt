package cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution

import org.hipparchus.distribution.continuous.AbstractRealDistribution
import java.lang.Math.*
import kotlin.math.pow
import kotlin.math.sqrt

class FinitedNormalDistribution2(val mean: Double, val sd: Double, val initHalfLimit: Double,
                                 solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) :
        AbstractRealDistribution(solverAbsoluteAccuracy) {

    private val coef: DoubleArray = doubleArrayOf(0.5, 0.75, 0.9375, 1.09375, 1.23046875, 1.353515625,
            1.46630859375, 1.571044921875, 1.6692352294921875, 1.76197052001953125, 1.8500690460205078125,
            1.93416309356689453125, 2.01475322246551513671875, 2.092243731021881103515625, 2.16696672141551971435546875,
            2.239198945462703704833984375, 2.30917391250841319561004638671875, 2.377090792288072407245635986328125,
            2.44312109207385219633579254150390625, 2.507413752391585148870944976806640625,
            2.570099096201374777592718601226806640625)

    val order: Int

    val revisedHalfLimit: Double

    val lowerLimit: Double

    val upperLimit: Double

    init {
        if (!mean.isFinite()) {
            throw IllegalArgumentException("Expected the parameter {mean} is finite,but get {mean = $mean}.")
        }
        if ((!sd.isFinite()) || sd < 0.0) {
            throw IllegalArgumentException("Expected the parameter {sd >= 0} and is finite,but get {sd = $sd}.")
        }
        if ((!initHalfLimit.isFinite()) || initHalfLimit <= 0.0) {
            throw IllegalArgumentException("Expected the parameter {initHalfLimit > 0} and is finite,but get {initHalfLimit = $initHalfLimit}.")
        }
        if (initHalfLimit <= 3 * sd) {
            this.order = 3
        } else {
            this.order = floor((pow(initHalfLimit / sd, 2.0) - 3.0) / 2.0).toInt()
        }
        if (this.order < 0 || this.order > 100) {
            throw RuntimeException("Expected {0 <= order <= 100},but get {order = $order}")
        }
        this.revisedHalfLimit = sd * sqrt(2 * order + 3.0)
        this.lowerLimit = mean - revisedHalfLimit
        this.upperLimit = mean + revisedHalfLimit
    }

    override fun getSupportLowerBound(): Double {
        return lowerLimit
    }

    override fun getSupportUpperBound(): Double {
        return upperLimit
    }

    override fun isSupportConnected(): Boolean {
        return true
    }

    override fun getNumericalMean(): Double {
        return mean
    }

    override fun getNumericalVariance(): Double {
        return pow(sd, 2.0)
    }

    override fun density(x: Double): Double {
        return when (order) {
            0 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            1 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            2 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            3 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            4 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            5 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            6 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            7 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            8 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            9 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            10 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            11 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            12 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            13 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            14 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            15 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            16 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            17 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            18 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            19 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            20 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            21 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            22 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            23 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            24 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            25 -> 0.5 * (((1.0 - pow((x - mean) / revisedHalfLimit, 2.0)).pow(order)) / revisedHalfLimit)
            else -> throw java.lang.RuntimeException("Nonsupport {FinitedNormalDistribution} when {order > 100}.")
        }
    }


    private fun cumulativeProbability(x: Double, order: Int): Double {
        val makeValue = -((mean - x) * ((revisedHalfLimit + mean - x).pow(order)) * ((revisedHalfLimit - mean + x).pow(order)) / (revisedHalfLimit.pow(2 * order + 1)))
        return when (order) {
            0 -> (revisedHalfLimit - mean + x) / (2.0 * revisedHalfLimit)
            1 -> 0.25 * makeValue + cumulativeProbability(x, order - 1)
            2 -> 0.1875 * makeValue + cumulativeProbability(x, order - 1)
            3 -> 0.15625 * makeValue + cumulativeProbability(x, order - 1)
            4 -> 0.13671875 * makeValue + cumulativeProbability(x, order - 1)
            5 -> 0.123046875 * makeValue + cumulativeProbability(x, order - 1)
            6 -> 0.11279296875 * makeValue + cumulativeProbability(x, order - 1)
            7 -> 0.104736328125 * makeValue + cumulativeProbability(x, order - 1)
            8 -> 0.0981903076171875 * makeValue + cumulativeProbability(x, order - 1)
            9 -> 0.09273529052734375 * makeValue + cumulativeProbability(x, order - 1)
            10 -> 0.0880985260009765625 * makeValue + cumulativeProbability(x, order - 1)
            11 -> 0.08409404754638671875 * makeValue + cumulativeProbability(x, order - 1)
            12 -> 0.08059012889862060546875 * makeValue + cumulativeProbability(x, order - 1)
            13 -> 0.077490508556365966796875 * makeValue + cumulativeProbability(x, order - 1)
            14 -> 0.07472299039363861083984375 * makeValue + cumulativeProbability(x, order - 1)
            15 -> 0.072232224047183990478515625 * makeValue + cumulativeProbability(x, order - 1)
            16 -> 0.06997496704570949077606201171875 * makeValue + cumulativeProbability(x, order - 1)
            17 -> 0.067916879779659211635589599609375 * makeValue + cumulativeProbability(x, order - 1)
            18 -> 0.06603029978577978909015655517578125 * makeValue + cumulativeProbability(x, order - 1)
            19 -> 0.064292660317732952535152435302734375 * makeValue + cumulativeProbability(x, order - 1)
            20 -> 0.062685343809789628721773624420166015625 * makeValue + cumulativeProbability(x, order - 1)
            else -> throw java.lang.RuntimeException("Nonsupport {FinitedNormalDistribution} when {order > 100}.")
        }
    }

    override fun cumulativeProbability(x: Double): Double {
        return cumulativeProbability(x, this.order)
    }
}