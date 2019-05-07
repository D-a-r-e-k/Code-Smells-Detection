/**
   * This applies each MessageFilter in filters array on the given
   * MessageProxy objects.
   *
   * @return a Vector containing the removed MessageProxy objects.
   */
public Vector applyFilters(List messages) {
    return applyFilters(messages, null);
}
