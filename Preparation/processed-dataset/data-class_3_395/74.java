/**
   * Calculate the false positive rate with respect to a particular class. This
   * is defined as
   * <p/>
   * 
   * <pre>
   * incorrectly classified negatives
   * --------------------------------
   *        total negatives
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the false positive rate
   */
public double falsePositiveRate(int classIndex) {
    return m_delegate.falsePositiveRate(classIndex);
}
