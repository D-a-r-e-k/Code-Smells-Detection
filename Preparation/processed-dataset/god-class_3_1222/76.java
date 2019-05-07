/**
   * Calculate number of false negatives with respect to a particular class.
   * This is defined as
   * <p/>
   * 
   * <pre>
   * incorrectly classified positives
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the false positive rate
   */
public double numFalseNegatives(int classIndex) {
    return m_delegate.numFalseNegatives(classIndex);
}
