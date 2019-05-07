/**
   * Calculate the number of true negatives with respect to a particular class.
   * This is defined as
   * <p/>
   * 
   * <pre>
   * correctly classified negatives
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the true positive rate
   */
public double numTrueNegatives(int classIndex) {
    return m_delegate.numTrueNegatives(classIndex);
}
