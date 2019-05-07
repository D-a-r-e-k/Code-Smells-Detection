/**
   * Unweighted micro-averaged F-measure. If some classes not present in the
   * test set, they have no effect.
   * 
   * Note: if the test set is *single-label*, then this is the same as accuracy.
   * 
   * @return unweighted micro-averaged F-measure.
   */
public double unweightedMicroFmeasure() {
    return m_delegate.unweightedMicroFmeasure();
}
