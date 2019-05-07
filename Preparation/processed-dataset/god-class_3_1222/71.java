/**
   * Calculate the true negative rate with respect to a particular class. This
   * is defined as
   * <p/>
   * 
   * <pre>
   * correctly classified negatives
   * ------------------------------
   *       total negatives
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the true positive rate
   */
public double trueNegativeRate(int classIndex) {
    return m_delegate.trueNegativeRate(classIndex);
}
