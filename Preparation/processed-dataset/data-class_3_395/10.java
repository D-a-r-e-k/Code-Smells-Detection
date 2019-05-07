/**
   * Returns the area under ROC for those predictions that have been collected
   * in the evaluateClassifier(Classifier, Instances) method. Returns
   * Utils.missingValue() if the area is not available.
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the area under the ROC curve or not a number
   */
public double areaUnderROC(int classIndex) {
    return m_delegate.areaUnderROC(classIndex);
}
