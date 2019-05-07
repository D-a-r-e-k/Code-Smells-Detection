/**
     * First try to find a grammar in the bucket, if failed, consult the
     * grammar pool. If a grammar is found in the pool, then add it (and all
     * imported ones) into the bucket.
     */
protected SchemaGrammar findGrammar(XSDDescription desc, boolean ignoreConflict) {
    SchemaGrammar sg = fGrammarBucket.getGrammar(desc.getTargetNamespace());
    if (sg == null) {
        if (fGrammarPool != null) {
            sg = (SchemaGrammar) fGrammarPool.retrieveGrammar(desc);
            if (sg != null) {
                // put this grammar into the bucket, along with grammars  
                // imported by it (directly or indirectly)  
                if (!fGrammarBucket.putGrammar(sg, true, ignoreConflict)) {
                    // REVISIT: a conflict between new grammar(s) and grammars  
                    // in the bucket. What to do? A warning? An exception?  
                    reportSchemaWarning("GrammarConflict", null, null);
                    sg = null;
                }
            }
        }
    }
    return sg;
}
