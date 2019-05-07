/**
   * Gets the number of test instances that had a known class value (actually
   * the sum of the weights of test instances with known class value).
   * 
   * @return the number of test instances with known class
   */
public final double numInstances() {
    return m_delegate.numInstances();
}
