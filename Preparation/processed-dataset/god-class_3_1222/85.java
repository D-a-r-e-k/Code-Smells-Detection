/**
   * Calculate the F-Measure with respect to a particular class. This is defined
   * as
   * <p/>
   * 
   * <pre>
   * 2 * recall * precision
   * ----------------------
   *   recall + precision
   * </pre>
   * 
   * @param classIndex the index of the class to consider as "positive"
   * @return the F-Measure
   */
public double fMeasure(int classIndex) {
    return m_delegate.fMeasure(classIndex);
}
