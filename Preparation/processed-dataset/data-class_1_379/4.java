/**
   * Extracts all terms texts of a given Query into an array of WeightedTerms
   *
   * @param query      Query to extract term texts from
   * @param prohibited <code>true</code> to extract "prohibited" terms, too
   * @return an array of the terms used in a query, plus their weights.
   */
public static final WeightedTerm[] getTerms(Query query, boolean prohibited) {
    return getTerms(query, prohibited, null);
}
