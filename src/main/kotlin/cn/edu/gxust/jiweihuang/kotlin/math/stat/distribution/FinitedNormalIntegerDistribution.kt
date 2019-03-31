package cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution

import org.hipparchus.distribution.discrete.AbstractIntegerDistribution
import kotlin.math.round

class FinitedNormalIntegerDistribution(val mean: Double, val halfLimit: Double, val order: Int = 3) :
        AbstractIntegerDistribution() {
    val finitedNormalDist = FinitedNormalDistribution(mean, halfLimit, order)

    override fun getSupportUpperBound(): Int {
        return round(mean - halfLimit).toInt()
    }

    override fun cumulativeProbability(x: Int): Double {
        return finitedNormalDist.cumulativeProbability(x.toDouble())
    }

    override fun getNumericalMean(): Double {
        return mean
    }

    override fun isSupportConnected(): Boolean {
        return true
    }

    override fun getNumericalVariance(): Double {
        return finitedNormalDist.numericalVariance
    }

    override fun probability(x: Int): Double {
        return probability(x - 1, x)
    }

    override fun getSupportLowerBound(): Int {
        return round(mean + halfLimit).toInt()
    }

}