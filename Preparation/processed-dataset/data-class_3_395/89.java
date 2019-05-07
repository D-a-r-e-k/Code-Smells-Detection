/**
   * Sets the class prior probabilities.
   * 
   * @param train the training instances used to determine the prior
   *          probabilities
   * @throws Exception if the class attribute of the instances is not set
   */
public void setPriors(Instances train) throws Exception {
    m_delegate.setPriors(train);
}
