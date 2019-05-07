/**
   * Returns the estimated error rate or the root mean squared error (if the
   * class is numeric). If a cost matrix was given this error rate gives the
   * average cost.
   * 
   * @return the estimated error rate (between 0 and 1, or between 0 and maximum
   *         cost)
   */
public final double errorRate() {
    return m_delegate.errorRate();
}
