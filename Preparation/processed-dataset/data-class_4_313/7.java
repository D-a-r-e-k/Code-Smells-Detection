/**
     * Pull the grammar out of the bucket simply using
     * its TNS as a key
     */
SchemaGrammar getGrammar(String tns) {
    return fGrammarBucket.getGrammar(tns);
}
