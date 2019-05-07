/**
   * Calculate number of false positives with respect to a particular class.
   * This is defined as
   * <p/>
   * 
   * <pre>
   * incorrectly classified negatives
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the false positive rate
   */
public double numFalsePositives(int classIndex) {
    return m_delegate.numFalsePositives(classIndex);
}
