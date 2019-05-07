/**
   * Evaluates the classifier on a single instance and records the prediction.
   * 
   * @param classifier machine learning classifier
   * @param instance the test instance to be classified
   * @return the prediction made by the clasifier
   * @throws Exception if model could not be evaluated successfully or the data
   *           contains string attributes
   */
public double evaluateModelOnceAndRecordPrediction(Classifier classifier, Instance instance) throws Exception {
    return m_delegate.evaluateModelOnceAndRecordPrediction(classifier, instance);
}
