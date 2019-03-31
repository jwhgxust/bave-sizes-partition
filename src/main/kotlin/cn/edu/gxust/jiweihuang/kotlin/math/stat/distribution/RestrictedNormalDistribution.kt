package cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution

import org.hipparchus.distribution.continuous.AbstractRealDistribution

abstract class RestrictedNormalDistribution protected constructor(val mean: Double = 0.0,
                                                                  val sd: Double = 1.0,
                                                                  val lowerLimit: Double = mean - 3.0 * sd,
                                                                  val upperLimit: Double = mean + 3.0 * sd,
                                                                  solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) :
        AbstractRealDistribution(DEFAULT_SOLVER_ABSOLUTE_ACCURACY) {

    init {
        if (!mean.isFinite()) {
            throw IllegalArgumentException("Expected the parameter {mean} is finite,but get {mean = $mean}.")
        }
        if ((!sd.isFinite()) || sd < 0.0) {
            throw IllegalArgumentException("Expected the parameter {sd >= 0} and is finite,but get {sd = $sd}.")
        }
        if ((!lowerLimit.isFinite()) || lowerLimit <= Double.NEGATIVE_INFINITY) {
            throw IllegalArgumentException("Expected the parameter {lowerLimit > Double.NEGATIVE_INFINITY} and is finite,but get {lowerLimit = $lowerLimit}.")
        }
        if (upperLimit <= lowerLimit) {
            throw IllegalArgumentException("Expected the parameter {lowerLimit < upperLimit},but get {lowerLimit = $lowerLimit,upperLimit = $upperLimit}.")
        }
        if ((!upperLimit.isFinite()) || upperLimit >= Double.POSITIVE_INFINITY) {
            throw IllegalArgumentException("Expected the parameter {upperLimit < Double.POSITIVE_INFINITY} and is finite,but get {upperLimit = $upperLimit}.")
        }
    }

    constructor(mean: Double = 0.0, sd: Double = 1.0, halfLimit: Double = 3.0 * sd,
                solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) : this(mean, sd,
            mean - halfLimit, mean + halfLimit, solverAbsoluteAccuracy)

    override fun getSupportLowerBound(): Double {
        return lowerLimit
    }

    override fun getSupportUpperBound(): Double {
        return upperLimit
    }

    override fun isSupportConnected(): Boolean {
        return true
    }
}