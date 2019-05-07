/**
   * disables the use of priors, e.g., in case of de-serialized schemes that
   * have no access to the original training set, but are evaluated on a set
   * set.
   */
public void useNoPriors() {
    m_delegate.useNoPriors();
}
