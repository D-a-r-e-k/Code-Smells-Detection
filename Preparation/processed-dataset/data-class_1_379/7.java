private static void getTermsFromFilteredQuery(FilteredQuery query, HashSet<WeightedTerm> terms, boolean prohibited, String fieldName) {
    getTerms(query.getQuery(), terms, prohibited, fieldName);
}
