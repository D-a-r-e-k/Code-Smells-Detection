/**
   * Gets the number of instances not classified (that is, for which no
   * prediction was made by the classifier). (Actually the sum of the weights of
   * these instances)
   * 
   * @return the number of unclassified instances
   */
public final double unclassified() {
    return m_delegate.unclassified();
}
