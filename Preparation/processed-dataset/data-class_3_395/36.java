/**
   * Gets the percentage of instances correctly classified (that is, for which a
   * correct prediction was made).
   * 
   * @return the percent of correctly classified instances (between 0 and 100)
   */
public final double pctCorrect() {
    return m_delegate.pctCorrect();
}
