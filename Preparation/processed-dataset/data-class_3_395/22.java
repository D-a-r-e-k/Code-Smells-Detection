/**
   * Evaluates the classifier on a single instance.
   * 
   * @param classifier machine learning classifier
   * @param instance the test instance to be classified
   * @return the prediction made by the clasifier
   * @throws Exception if model could not be evaluated successfully or the data
   *           contains string attributes
   */
public double evaluateModelOnce(Classifier classifier, Instance instance) throws Exception {
    return m_delegate.evaluateModelOnce(classifier, instance);
}
