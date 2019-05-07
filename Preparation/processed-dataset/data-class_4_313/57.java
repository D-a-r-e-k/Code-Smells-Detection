private SchemaGrammar createGrammarFrom(SchemaGrammar grammar) {
    SchemaGrammar newGrammar = new SchemaGrammar(grammar);
    fGrammarBucket.putGrammar(newGrammar);
    // update all the grammars in the bucket to point to the new grammar.  
    updateImportListWith(newGrammar);
    // update import list of the new grammar  
    updateImportListFor(newGrammar);
    return newGrammar;
}
