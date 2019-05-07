/**
   * Returns the area under precision-recall curve (AUPRC) for those predictions
   * that have been collected in the evaluateClassifier(Classifier, Instances)
   * method. Returns Utils.missingValue() if the area is not available.
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the area under the precision-recall curve or not a number
   */
public double areaUnderPRC(int classIndex) {
    return m_delegate.areaUnderPRC(classIndex);
}
