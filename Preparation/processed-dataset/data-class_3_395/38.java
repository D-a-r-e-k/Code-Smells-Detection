/**
   * Gets the percentage of instances not classified (that is, for which no
   * prediction was made by the classifier).
   * 
   * @return the percent of unclassified instances (between 0 and 100)
   */
public final double pctUnclassified() {
    return m_delegate.pctUnclassified();
}
