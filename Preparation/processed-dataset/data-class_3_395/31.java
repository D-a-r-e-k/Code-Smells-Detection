/**
   * Gets the number of instances incorrectly classified (that is, for which an
   * incorrect prediction was made). (Actually the sum of the weights of these
   * instances)
   * 
   * @return the number of incorrectly classified instances
   */
public final double incorrect() {
    return m_delegate.incorrect();
}
