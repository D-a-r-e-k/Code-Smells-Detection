/**
   * Outputs the performance statistics in summary form. Lists number (and
   * percentage) of instances classified correctly, incorrectly and
   * unclassified. Outputs the total number of instances classified, and the
   * number of instances (if any) that had no class value provided.
   * 
   * @param title the title for the statistics
   * @param printComplexityStatistics if true, complexity statistics are
   *          returned as well
   * @return the summary as a String
   */
public String toSummaryString(String title, boolean printComplexityStatistics) {
    return m_delegate.toSummaryString(title, printComplexityStatistics);
}
