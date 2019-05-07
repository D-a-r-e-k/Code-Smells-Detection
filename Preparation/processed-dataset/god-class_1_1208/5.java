private static final void getTerms(Query query, HashSet<WeightedTerm> terms, boolean prohibited, String fieldName) {
    try {
        if (query instanceof BooleanQuery)
            getTermsFromBooleanQuery((BooleanQuery) query, terms, prohibited, fieldName);
        else if (query instanceof FilteredQuery)
            getTermsFromFilteredQuery((FilteredQuery) query, terms, prohibited, fieldName);
        else {
            HashSet<Term> nonWeightedTerms = new HashSet<Term>();
            query.extractTerms(nonWeightedTerms);
            for (Iterator<Term> iter = nonWeightedTerms.iterator(); iter.hasNext(); ) {
                Term term = iter.next();
                if ((fieldName == null) || (term.field().equals(fieldName))) {
                    terms.add(new WeightedTerm(query.getBoost(), term.text()));
                }
            }
        }
    } catch (UnsupportedOperationException ignore) {
    }
}
