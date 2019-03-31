package cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution


import org.hipparchus.distribution.continuous.NormalDistribution
import org.hipparchus.random.RandomDataGenerator
import java.lang.RuntimeException

class PickedNormalDistribution(mean: Double = 0.0, sd: Double = 1.0,
                               lowerLimit: Double = mean - 3.0 * sd,
                               upperLimit: Double = mean + 3.0 * sd,
                               solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) :
        RestrictedNormalDistribution(mean, sd, lowerLimit, upperLimit, solverAbsoluteAccuracy) {

    constructor(mean: Double = 0.0, sd: Double = 1.0, halfLimit: Double = 3.0 * sd,
                solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) : this(mean, sd,
            mean - halfLimit, mean + halfLimit, solverAbsoluteAccuracy)

    val normalDist: NormalDistribution = NormalDistribution(mean, sd)

    override fun density(x: Double): Double {
        return normalDist.density(x)
    }

    override fun cumulativeProbability(x: Double): Double {
        return normalDist.cumulativeProbability(x) - normalDist.cumulativeProbability(lowerLimit)
    }

    override fun getNumericalMean(): Double {
        return mean
    }

    override fun getNumericalVariance(): Double {
        return sd
    }

    fun nextPickedNormal(randomDataGenerator: RandomDataGenerator = RandomDataGenerator()): Double {
        var result: Double?
        do {
            try {
                result = randomDataGenerator.nextDeviate(this)
            } catch (e: RuntimeException) {
                result = null
            }
        } while (result == null)
        return result
    }
}

fun nextPickedNormal(mean: Double = 0.0, sd: Double = 1.0,
                     lowerLimit: Double = mean - 3.0 * sd,
                     upperLimit: Double = mean + 3.0 * sd,
                     randomDataGenerator: RandomDataGenerator = RandomDataGenerator()): Double {
    var result: Double
    do {
        result = randomDataGenerator.nextNormal(mean, sd)
    } while (result !in lowerLimit..upperLimit)
    return result
}

fun nextPickedNormal(mean: Double = 0.0, sd: Double = 1.0, halfLimit: Double = 3.0 * sd,
                     randomDataGenerator: RandomDataGenerator = RandomDataGenerator()): Double {
    return nextPickedNormal(mean, sd, mean - halfLimit, mean + halfLimit, randomDataGenerator)
}