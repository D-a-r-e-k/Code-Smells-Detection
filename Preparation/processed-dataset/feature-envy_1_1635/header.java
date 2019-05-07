void method0() { 
/** for serialization */
static final long serialVersionUID = -8246163625699362456L;
/** The Lagrange multipliers. */
protected double[] m_alpha;
/** The thresholds. */
protected double m_b, m_bLow, m_bUp;
/** The indices for m_bLow and m_bUp */
protected int m_iLow, m_iUp;
/** The training data. */
protected Instances m_data;
/** Weight vector for linear machine. */
protected double[] m_weights;
/** Variables to hold weight vector in sparse form.
	(To reduce storage requirements.) */
protected double[] m_sparseWeights;
protected int[] m_sparseIndices;
/** Kernel to use **/
protected Kernel m_kernel;
/** The transformed class values. */
protected double[] m_class;
/** The current set of errors for all non-bound examples. */
protected double[] m_errors;
/* The five different sets used by the algorithm. */
/** {i: 0 < m_alpha[i] < C} */
protected SMOset m_I0;
/**  {i: m_class[i] = 1, m_alpha[i] = 0} */
protected SMOset m_I1;
/**  {i: m_class[i] = -1, m_alpha[i] =C} */
protected SMOset m_I2;
/** {i: m_class[i] = 1, m_alpha[i] = C} */
protected SMOset m_I3;
/**  {i: m_class[i] = -1, m_alpha[i] = 0} */
protected SMOset m_I4;
/** The set of support vectors */
protected SMOset m_supportVectors;
// {i: 0 < m_alpha[i]} 
/** Stores logistic regression model for probability estimate */
protected Logistic m_logistic = null;
/** Stores the weight of the training instances */
protected double m_sumOfWeights = 0;
}
