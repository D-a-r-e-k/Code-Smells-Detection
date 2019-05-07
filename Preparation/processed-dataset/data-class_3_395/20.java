/**
   * Evaluates the supplied distribution on a single instance.
   * 
   * @param dist the supplied distribution
   * @param instance the test instance to be classified
   * @param storePredictions whether to store predictions for nominal classifier
   * @return the prediction
   * @throws Exception if model could not be evaluated successfully
   */
public double evaluationForSingleInstance(double[] dist, Instance instance, boolean storePredictions) throws Exception {
    return m_delegate.evaluationForSingleInstance(dist, instance, storePredictions);
}
