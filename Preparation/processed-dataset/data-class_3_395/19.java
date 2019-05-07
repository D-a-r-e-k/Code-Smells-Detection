/**
   * Evaluates the classifier on a given set of instances. Note that the data
   * must have exactly the same format (e.g. order of attributes) as the data
   * used to train the classifier! Otherwise the results will generally be
   * meaningless.
   * 
   * @param classifier machine learning classifier
   * @param data set of test instances for evaluation
   * @param forPredictionsPrinting varargs parameter that, if supplied, is
   *          expected to hold a
   *          weka.classifiers.evaluation.output.prediction.AbstractOutput
   *          object
   * @return the predictions
   * @throws Exception if model could not be evaluated successfully
   */
public double[] evaluateModel(Classifier classifier, Instances data, Object... forPredictionsPrinting) throws Exception {
    return m_delegate.evaluateModel(classifier, data, forPredictionsPrinting);
}
