/**
     * Method for building the binary classifier.
     *
     * @param insts the set of training instances
     * @param cl1 the first class' index
     * @param cl2 the second class' index
     * @param fitLogistic true if logistic model is to be fit
     * @param numFolds number of folds for internal cross-validation
     * @param randomSeed random number generator for cross-validation
     * @throws Exception if the classifier can't be built successfully
     */
protected void buildClassifier(Instances insts, int cl1, int cl2, boolean fitLogistic, int numFolds, int randomSeed) throws Exception {
    // Initialize some variables 
    m_bUp = -1;
    m_bLow = 1;
    m_b = 0;
    m_alpha = null;
    m_data = null;
    m_weights = null;
    m_errors = null;
    m_logistic = null;
    m_I0 = null;
    m_I1 = null;
    m_I2 = null;
    m_I3 = null;
    m_I4 = null;
    m_sparseWeights = null;
    m_sparseIndices = null;
    // Store the sum of weights 
    m_sumOfWeights = insts.sumOfWeights();
    // Set class values 
    m_class = new double[insts.numInstances()];
    m_iUp = -1;
    m_iLow = -1;
    for (int i = 0; i < m_class.length; i++) {
        if ((int) insts.instance(i).classValue() == cl1) {
            m_class[i] = -1;
            m_iLow = i;
        } else if ((int) insts.instance(i).classValue() == cl2) {
            m_class[i] = 1;
            m_iUp = i;
        } else {
            throw new Exception("This should never happen!");
        }
    }
    // Check whether one or both classes are missing 
    if ((m_iUp == -1) || (m_iLow == -1)) {
        if (m_iUp != -1) {
            m_b = -1;
        } else if (m_iLow != -1) {
            m_b = 1;
        } else {
            m_class = null;
            return;
        }
        if (m_KernelIsLinear) {
            m_sparseWeights = new double[0];
            m_sparseIndices = new int[0];
            m_class = null;
        } else {
            m_supportVectors = new SMOset(0);
            m_alpha = new double[0];
            m_class = new double[0];
        }
        // Fit sigmoid if requested 
        if (fitLogistic) {
            fitLogistic(insts, cl1, cl2, numFolds, new Random(randomSeed));
        }
        return;
    }
    // Set the reference to the data 
    m_data = insts;
    // If machine is linear, reserve space for weights 
    if (m_KernelIsLinear) {
        m_weights = new double[m_data.numAttributes()];
    } else {
        m_weights = null;
    }
    // Initialize alpha array to zero 
    m_alpha = new double[m_data.numInstances()];
    // Initialize sets 
    m_supportVectors = new SMOset(m_data.numInstances());
    m_I0 = new SMOset(m_data.numInstances());
    m_I1 = new SMOset(m_data.numInstances());
    m_I2 = new SMOset(m_data.numInstances());
    m_I3 = new SMOset(m_data.numInstances());
    m_I4 = new SMOset(m_data.numInstances());
    // Clean out some instance variables 
    m_sparseWeights = null;
    m_sparseIndices = null;
    // init kernel 
    m_kernel.buildKernel(m_data);
    // Initialize error cache 
    m_errors = new double[m_data.numInstances()];
    m_errors[m_iLow] = 1;
    m_errors[m_iUp] = -1;
    // Build up I1 and I4 
    for (int i = 0; i < m_class.length; i++) {
        if (m_class[i] == 1) {
            m_I1.insert(i);
        } else {
            m_I4.insert(i);
        }
    }
    // Loop to find all the support vectors 
    int numChanged = 0;
    boolean examineAll = true;
    while ((numChanged > 0) || examineAll) {
        numChanged = 0;
        if (examineAll) {
            for (int i = 0; i < m_alpha.length; i++) {
                if (examineExample(i)) {
                    numChanged++;
                }
            }
        } else {
            // This code implements Modification 1 from Keerthi et al.'s paper 
            for (int i = 0; i < m_alpha.length; i++) {
                if ((m_alpha[i] > 0) && (m_alpha[i] < m_C * m_data.instance(i).weight())) {
                    if (examineExample(i)) {
                        numChanged++;
                    }
                    // Is optimality on unbound vectors obtained? 
                    if (m_bUp > m_bLow - 2 * m_tol) {
                        numChanged = 0;
                        break;
                    }
                }
            }
        }
        if (examineAll) {
            examineAll = false;
        } else if (numChanged == 0) {
            examineAll = true;
        }
    }
    // Set threshold 
    m_b = (m_bLow + m_bUp) / 2.0;
    // Save memory 
    m_kernel.clean();
    m_errors = null;
    m_I0 = m_I1 = m_I2 = m_I3 = m_I4 = null;
    // If machine is linear, delete training data 
    // and store weight vector in sparse format 
    if (m_KernelIsLinear) {
        // We don't need to store the set of support vectors 
        m_supportVectors = null;
        // We don't need to store the class values either 
        m_class = null;
        // Clean out training data 
        if (!m_checksTurnedOff) {
            m_data = new Instances(m_data, 0);
        } else {
            m_data = null;
        }
        // Convert weight vector 
        double[] sparseWeights = new double[m_weights.length];
        int[] sparseIndices = new int[m_weights.length];
        int counter = 0;
        for (int i = 0; i < m_weights.length; i++) {
            if (m_weights[i] != 0.0) {
                sparseWeights[counter] = m_weights[i];
                sparseIndices[counter] = i;
                counter++;
            }
        }
        m_sparseWeights = new double[counter];
        m_sparseIndices = new int[counter];
        System.arraycopy(sparseWeights, 0, m_sparseWeights, 0, counter);
        System.arraycopy(sparseIndices, 0, m_sparseIndices, 0, counter);
        // Clean out weight vector 
        m_weights = null;
        // We don't need the alphas in the linear case 
        m_alpha = null;
    }
    // Fit sigmoid if requested 
    if (fitLogistic) {
        fitLogistic(insts, cl1, cl2, numFolds, new Random(randomSeed));
    }
}
