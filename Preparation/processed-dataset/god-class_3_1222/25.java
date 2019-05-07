/**
   * Evaluates the supplied prediction on a single instance.
   * 
   * @param prediction the supplied prediction
   * @param instance the test instance to be classified
   * @throws Exception if model could not be evaluated successfully
   */
public void evaluateModelOnce(double prediction, Instance instance) throws Exception {
    m_delegate.evaluateModelOnce(prediction, instance);
}
