/**
   * Returns the list of plugin metrics in use (or null if there are none)
   * 
   * @return the list of plugin metrics
   */
public List<AbstractEvaluationMetric> getPluginMetrics() {
    return m_delegate.getPluginMetrics();
}
