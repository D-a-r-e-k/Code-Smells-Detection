/**
   * Calculate the true positive rate with respect to a particular class. This
   * is defined as
   * <p/>
   * 
   * <pre>
   * correctly classified positives
   * ------------------------------
   *       total positives
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the true positive rate
   */
public double truePositiveRate(int classIndex) {
    return m_delegate.truePositiveRate(classIndex);
}
