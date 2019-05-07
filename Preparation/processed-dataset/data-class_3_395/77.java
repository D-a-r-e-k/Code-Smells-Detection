/**
   * Calculate the false negative rate with respect to a particular class. This
   * is defined as
   * <p/>
   * 
   * <pre>
   * incorrectly classified positives
   * --------------------------------
   *        total positives
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the false positive rate
   */
public double falseNegativeRate(int classIndex) {
    return m_delegate.falseNegativeRate(classIndex);
}
