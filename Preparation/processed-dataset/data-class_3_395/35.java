/**
   * Gets the number of instances correctly classified (that is, for which a
   * correct prediction was made). (Actually the sum of the weights of these
   * instances)
   * 
   * @return the number of correctly classified instances
   */
public final double correct() {
    return m_delegate.correct();
}
