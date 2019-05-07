/**
   * Evaluates the supplied distribution on a single instance.
   * 
   * @param dist the supplied distribution
   * @param instance the test instance to be classified
   * @return the prediction
   * @throws Exception if model could not be evaluated successfully
   */
public double evaluateModelOnce(double[] dist, Instance instance) throws Exception {
    return m_delegate.evaluateModelOnce(dist, instance);
}
