/**
   * Calculate the precision with respect to a particular class. This is defined
   * as
   * <p/>
   * 
   * <pre>
   * correctly classified positives
   * ------------------------------
   *  total predicted as positive
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the precision
   */
public double precision(int classIndex) {
    return m_delegate.precision(classIndex);
}
