/**
   * Unweighted macro-averaged F-measure. If some classes not present in the
   * test set, they're just skipped (since recall is undefined there anyway) .
   * 
   * @return unweighted macro-averaged F-measure.
   * */
public double unweightedMacroFmeasure() {
    return m_delegate.unweightedMacroFmeasure();
}
