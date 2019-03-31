package cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution

import org.hipparchus.distribution.continuous.NormalDistribution
import org.hipparchus.special.Erf
import java.lang.Math.pow
import kotlin.math.exp
import kotlin.math.sqrt

/**
 * Reference:[Truncated normal distribution](https://en.wikipedia.org/wiki/Truncated_normal_distribution)
 */
class TruncatedNormalDistribution(mean: Double = 0.0, sd: Double = 1.0,
                                  lowerLimit: Double = mean - 3.0 * sd,
                                  upperLimit: Double = mean + 3.0 * sd,
                                  solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) :
        RestrictedNormalDistribution(mean, sd, lowerLimit, upperLimit, solverAbsoluteAccuracy) {

    constructor(mean: Double = 0.0, sd: Double = 1.0, halfLimit: Double = 3.0 * sd,
                solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) : this(mean, sd,
            mean - halfLimit, mean + halfLimit, solverAbsoluteAccuracy)

    val halfLimit: Double
        get() = (upperLimit - lowerLimit) / 2.0

    /**
     * Reference:[standard normal distribution](https://en.wikipedia.org/wiki/Normal_distribution)
     */
    val standardNormalDist = NormalDistribution()

    /**
     * Greek letter $\alpha$
     */
    val alpha: Double = (lowerLimit - mean) / sd
    /**
     * Greek letter $\beta$
     */
    val beta: Double = (upperLimit - mean) / sd

    /**
     * Capital letter $Z$
     */
    val zUC: Double = phiUC(beta) - phiUC(alpha)

    /**
     * Greek letter $\xi$
     */
    fun xi(x: Double): Double {
        return (x - mean) / sd
    }

    /**
     * Greek letter (lower case) $\phi$
     */
    fun phiLC(x: Double): Double {
        return exp(-pow(x, 2.0) / 2.0) / sqrt(2.0 * Math.PI)
    }

    /**
     * Greek letter (upper case) $\phi$
     */
    fun phiUC(x: Double): Double {
        return (Erf.erf(x / sqrt(2.0)) + 1.0) / 2.0
    }

    /**
     * PDF: probability density function
     */
    override fun density(x: Double): Double {
        return phiLC(xi(x)) / (sd * zUC)
    }

    /**
     * CDF:cumulative distribution function
     */
    override fun cumulativeProbability(x: Double): Double {
        return (phiUC(xi(x)) - phiUC(alpha)) / zUC
    }

    /**
     * Mean
     */
    override fun getNumericalMean(): Double {
        return mean + ((phiLC(alpha) - phiLC(beta)) / zUC) * sd
    }

    /**
     * Variance
     */
    override fun getNumericalVariance(): Double {
        return pow(sd, 2.0) * (1.0 + (alpha * phiLC(alpha) -
                beta * phiLC(beta)) / zUC - pow((phiLC(alpha) -
                phiLC(beta)) / zUC, 2.0))
    }
}