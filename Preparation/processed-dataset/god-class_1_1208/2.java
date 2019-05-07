/**
   * Extracts all terms texts of a given Query into an array of WeightedTerms
   *
   * @param query      Query to extract term texts from
   * @param reader used to compute IDF which can be used to a) score selected fragments better
   * b) use graded highlights eg changing intensity of font color
   * @param fieldName the field on which Inverse Document Frequency (IDF) calculations are based
   * @return an array of the terms used in a query, plus their weights.
   */
public static final WeightedTerm[] getIdfWeightedTerms(Query query, IndexReader reader, String fieldName) {
    WeightedTerm[] terms = getTerms(query, false, fieldName);
    int totalNumDocs = reader.maxDoc();
    for (int i = 0; i < terms.length; i++) {
        try {
            int docFreq = reader.docFreq(new Term(fieldName, terms[i].term));
            //IDF algorithm taken from DefaultSimilarity class 
            float idf = (float) (Math.log(totalNumDocs / (double) (docFreq + 1)) + 1.0);
            terms[i].weight *= idf;
        } catch (IOException e) {
        }
    }
    return terms;
}
