/**
   * Return the total Kononenko & Bratko Information score in bits.
   * 
   * @return the K&B information score
   * @throws Exception if the class is not nominal
   */
public final double KBInformation() throws Exception {
    return m_delegate.KBInformation();
}
