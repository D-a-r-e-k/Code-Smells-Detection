/**
   * Calculate the recall with respect to a particular class. This is defined as
   * <p/>
   * 
   * <pre>
   * correctly classified positives
   * ------------------------------
   *       total positives
   * </pre>
   * <p/>
   * (Which is also the same as the truePositiveRate.)
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the recall
   */
public double recall(int classIndex) {
    return m_delegate.recall(classIndex);
}
