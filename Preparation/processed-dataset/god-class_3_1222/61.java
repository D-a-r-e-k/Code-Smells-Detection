/**
   * Calls toSummaryString() with a default title.
   * 
   * @param printComplexityStatistics if true, complexity statistics are
   *          returned as well
   * @return the summary string
   */
public String toSummaryString(boolean printComplexityStatistics) {
    return m_delegate.toSummaryString(printComplexityStatistics);
}
