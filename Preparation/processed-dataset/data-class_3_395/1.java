/**
   * Utility method to get a list of the names of all built-in and plugin
   * evaluation metrics
   * 
   * @return the complete list of available evaluation metrics
   */
public static List<String> getAllEvaluationMetricNames() {
    return weka.classifiers.evaluation.Evaluation.getAllEvaluationMetricNames();
}
