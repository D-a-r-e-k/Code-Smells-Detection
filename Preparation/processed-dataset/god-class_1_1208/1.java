/**
   * Extracts all terms texts of a given Query into an array of WeightedTerms
   *
   * @param query      Query to extract term texts from
   * @return an array of the terms used in a query, plus their weights.
   */
public static final WeightedTerm[] getTerms(Query query) {
    return getTerms(query, false);
}
