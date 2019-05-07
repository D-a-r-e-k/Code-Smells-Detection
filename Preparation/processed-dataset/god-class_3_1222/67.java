/**
   * Calculate the number of true positives with respect to a particular class.
   * This is defined as
   * <p/>
   * 
   * <pre>
   * correctly classified positives
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the true positive rate
   */
public double numTruePositives(int classIndex) {
    return m_delegate.numTruePositives(classIndex);
}
