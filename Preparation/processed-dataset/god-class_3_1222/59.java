/**
   * Output the cumulative margin distribution as a string suitable for input
   * for gnuplot or similar package.
   * 
   * @return the cumulative margin distribution
   * @throws Exception if the class attribute is nominal
   */
public String toCumulativeMarginDistributionString() throws Exception {
    return m_delegate.toCumulativeMarginDistributionString();
}
