/**
   * Get the named plugin evaluation metric
   * 
   * @param name the name of the metric (as returned by
   *          AbstractEvaluationMetric.getName()) or the fully qualified class
   *          name of the metric to find
   * 
   * @return the metric or null if the metric is not in the list of plugin
   *         metrics
   */
public AbstractEvaluationMetric getPluginMetric(String name) {
    return m_delegate.getPluginMetric(name);
}
