/**
   * Sets whether to discard predictions, ie, not storing them for future
   * reference via predictions() method in order to conserve memory.
   * 
   * @param value true if to discard the predictions
   * @see #predictions()
   */
public void setDiscardPredictions(boolean value) {
    m_delegate.setDiscardPredictions(value);
}
