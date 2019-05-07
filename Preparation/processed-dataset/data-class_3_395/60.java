/**
   * Calls toSummaryString() with no title and no complexity stats.
   * 
   * @return a summary description of the classifier evaluation
   */
@Override
public String toSummaryString() {
    return m_delegate.toSummaryString();
}
