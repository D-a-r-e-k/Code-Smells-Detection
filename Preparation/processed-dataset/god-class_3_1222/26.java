/**
   * Returns the predictions that have been collected.
   * 
   * @return a reference to the FastVector containing the predictions that have
   *         been collected. This should be null if no predictions have been
   *         collected.
   */
public FastVector predictions() {
    return m_delegate.predictions();
}
