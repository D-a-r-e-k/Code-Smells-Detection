/**
   * Returns whether predictions are not recorded at all, in order to conserve
   * memory.
   * 
   * @return true if predictions are not recorded
   * @see #predictions()
   */
public boolean getDiscardPredictions() {
    return m_delegate.getDiscardPredictions();
}
