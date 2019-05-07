/**
   * Set a list of the names of metrics to have appear in the output. The
   * default is to display all built in metrics and plugin metrics that haven't
   * been globally disabled.
   * 
   * @param display a list of metric names to have appear in the output
   */
public void setMetricsToDisplay(List<String> display) {
    m_delegate.setMetricsToDisplay(display);
}
