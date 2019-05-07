/**
   * extractTerms is currently the only query-independent means of introspecting queries but it only reveals
   * a list of terms for that query - not the boosts each individual term in that query may or may not have.
   * "Container" queries such as BooleanQuery should be unwrapped to get at the boost info held
   * in each child element.
   * Some discussion around this topic here:
   * http://www.gossamer-threads.com/lists/lucene/java-dev/34208?search_string=introspection;#34208
   * Unfortunately there seemed to be limited interest in requiring all Query objects to implement
   * something common which would allow access to child queries so what follows here are query-specific
   * implementations for accessing embedded query elements.
   */
private static final void getTermsFromBooleanQuery(BooleanQuery query, HashSet<WeightedTerm> terms, boolean prohibited, String fieldName) {
    BooleanClause[] queryClauses = query.getClauses();
    for (int i = 0; i < queryClauses.length; i++) {
        if (prohibited || queryClauses[i].getOccur() != BooleanClause.Occur.MUST_NOT)
            getTerms(queryClauses[i].getQuery(), terms, prohibited, fieldName);
    }
}
