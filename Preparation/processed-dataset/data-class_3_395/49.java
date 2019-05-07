/**
   * Calculate the entropy of the prior distribution.
   * 
   * @return the entropy of the prior distribution
   * @throws Exception if the class is not nominal
   */
public final double priorEntropy() throws Exception {
    return m_delegate.priorEntropy();
}
