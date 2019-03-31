package cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution

import org.hipparchus.distribution.continuous.NormalDistribution
import org.hipparchus.distribution.discrete.AbstractIntegerDistribution

class PickedNormalIntegerDistribution(mean: Double, sd: Double) : AbstractIntegerDistribution() {

    private val normalDistribution: NormalDistribution = NormalDistribution(mean, sd)

    override fun getSupportLowerBound(): Int {
        return Int.MIN_VALUE
    }

    override fun getSupportUpperBound(): Int {
        return Int.MAX_VALUE
    }

    override fun isSupportConnected(): Boolean {
        return true
    }

    override fun probability(x: Int): Double {
        return probability(x-1, x)
    }

    override fun cumulativeProbability(x: Int): Double {
        return normalDistribution.cumulativeProbability(x.toDouble())
    }

    override fun getNumericalMean(): Double {
        return normalDistribution.mean
    }

    override fun getNumericalVariance(): Double {
        return normalDistribution.numericalVariance
    }

}