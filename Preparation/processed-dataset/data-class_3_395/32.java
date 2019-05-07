/**
   * Gets the percentage of instances incorrectly classified (that is, for which
   * an incorrect prediction was made).
   * 
   * @return the percent of incorrectly classified instances (between 0 and 100)
   */
public final double pctIncorrect() {
    return m_delegate.pctIncorrect();
}
