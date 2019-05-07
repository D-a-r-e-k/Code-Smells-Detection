void method0() { 
/** The collection used to store the weighted values. */
protected TreeMap<Double, Double> m_TM = new TreeMap<Double, Double>();
/** The weighted sum of values */
protected double m_WeightedSum = 0;
/** The weighted sum of squared values */
protected double m_WeightedSumSquared = 0;
/** The weight of the values collected so far */
protected double m_SumOfWeights = 0;
/** The current bandwidth (only computed when needed) */
protected double m_Width = Double.MAX_VALUE;
/** The exponent to use in computation of bandwidth (default: -0.25) */
protected double m_Exponent = -0.25;
/** The minimum allowed value of the kernel width (default: 1.0E-6) */
protected double m_MinWidth = 1.0E-6;
/** Constant for Gaussian density. */
public static final double CONST = -0.5 * Math.log(2 * Math.PI);
/** Threshold at which further kernels are no longer added to sum. */
protected double m_Threshold = 1.0E-6;
/** The number of intervals used to approximate prediction interval. */
protected int m_NumIntervals = 1000;
}
