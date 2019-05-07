/**
   * Return the Kononenko & Bratko Information score in bits per instance.
   * 
   * @return the K&B information score
   * @throws Exception if the class is not nominal
   */
public final double KBMeanInformation() throws Exception {
    return m_delegate.KBMeanInformation();
}
