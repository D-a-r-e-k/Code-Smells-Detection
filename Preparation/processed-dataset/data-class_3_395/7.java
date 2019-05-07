/**
   * Remove the supplied list of metrics from the list of those to display.
   * 
   * @param metricsNotToDisplay
   */
public void dontDisplayMetrics(List<String> metricsNotToDisplay) {
    m_delegate.dontDisplayMetrics(metricsNotToDisplay);
}
