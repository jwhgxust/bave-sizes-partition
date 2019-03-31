package cn.edu.gxust.jiweihuang.kotlin.math.stat.random

import cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution.PickedNormalIntegerDistribution

class RandomDataGenerator : org.hipparchus.random.RandomDataGenerator {
    constructor() : super()
    constructor(seed: Long) : super(seed)

    /**
     * Generate normally distributed random integer numbers.
     *
     *
     * It generates random numbers without boundaries specified.
     *
     * it's worth noting that The acquisition of integers has taken advantage of
     * {java.lang.Math.round} method,it could lead to inaccuracy of normal distribution.
     *
     * @param mean              The mean of normal distribution.
     * @param standardDeviation The standard deviation of normal distribution.
     * @return normally distributed random integer number.
     */
    fun nextNormalInt(mean: Double, standardDeviation: Double): Int {
        return nextDeviate(PickedNormalIntegerDistribution(mean, standardDeviation))
    }

    /**
     *
     * Generate normally distributed random integer numbers with boundaries specified by
     * lower limit and upper limit of random integer number.
     *
     *
     * It's risky,because the normal distribution is symmetrical and the axis of symmetry is
     * mean of normal distribution,if the lower and upper limit is not symmetric about
     * the mean of normal distribution, The result distribution will be not a normal distribution.
     * So,the lower and upper limit should be equidistant with mean of normal distribution.
     *
     *
     * Try to use method `int nextNormalLimitedIntBias(double mean, double standardDeviation, double bias)` or
     * `int nextNormalLimitedIntScale(double mean, double standardDeviation,double magnification)`.
     *
     * @param mean              The mean of normal distribution.
     * @param standardDeviation The standard deviation of normal distribution.
     * @param lower             The lower limit of random integer number.
     * @param upper             The upper limit of random integer number.
     * @return normally distributed random integer number.
     */
    fun nextNormalLimitedInt(mean: Double, standardDeviation: Double, lower: Int, upper: Int): Int {
        if (lower >= Math.round(mean).toInt()) {
            throw IllegalArgumentException("Expected the parameter {lower < ${Math.round(mean)}},but get {lower = $lower}.")
        }
        if (upper <= Math.round(mean).toInt()) {
            throw IllegalArgumentException("Expected the parameter {upper > ${Math.round(mean)}},but get {upper = $upper}.")
        }
        var makeInt: Int
        do {
            makeInt = nextNormalInt(mean, standardDeviation)
        } while (makeInt < lower || makeInt > upper)
        return makeInt
    }

    /**
     * Generate normally distributed random integer numbers with boundaries specified
     * by bias from the mean.
     *
     * @param mean              The mean of normal distribution.
     * @param standardDeviation The standard deviation of normal distribution.
     * @param bias              bias from the mean
     * @return normally distributed random integer number.
     */
    fun nextNormalLimitedIntBias(mean: Double, standardDeviation: Double, bias: Int): Int {
        return nextNormalLimitedInt(
                mean, standardDeviation,
                Math.round(mean - bias).toInt(),
                Math.round(mean + bias).toInt()
        )
    }

    /**
     * Generate normally distributed random integer numbers with boundaries specified
     * by scale of standard deviation from mean.
     *
     * @param mean              The mean of normal distribution.
     * @param standardDeviation The standard deviation of normal distribution.
     * @param scale     scale of standard deviation from mean.
     * @return normally distributed random integer number.
     */
    fun nextNormalLimitedIntScale(mean: Double, standardDeviation: Double, scale: Double): Int {
        return nextNormalLimitedInt(
                mean, standardDeviation,
                Math.round(mean - scale * standardDeviation).toInt(),
                Math.round(mean + scale * standardDeviation).toInt()
        )
    }
}