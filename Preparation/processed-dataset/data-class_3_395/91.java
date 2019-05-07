/**
   * Updates the class prior probabilities or the mean respectively (when
   * incrementally training).
   * 
   * @param instance the new training instance seen
   * @throws Exception if the class of the instance is not set
   */
public void updatePriors(Instance instance) throws Exception {
    m_delegate.updatePriors(instance);
}
