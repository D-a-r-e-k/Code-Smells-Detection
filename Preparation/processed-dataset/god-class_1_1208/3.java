/**
   * Extracts all terms texts of a given Query into an array of WeightedTerms
   *
   * @param query      Query to extract term texts from
   * @param prohibited <code>true</code> to extract "prohibited" terms, too
   * @param fieldName  The fieldName used to filter query terms
   * @return an array of the terms used in a query, plus their weights.
   */
public static final WeightedTerm[] getTerms(Query query, boolean prohibited, String fieldName) {
    HashSet<WeightedTerm> terms = new HashSet<WeightedTerm>();
    getTerms(query, terms, prohibited, fieldName);
    return terms.toArray(new WeightedTerm[0]);
}
