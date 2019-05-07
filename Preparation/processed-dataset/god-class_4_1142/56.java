private void addGrammarComponents(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    if (dstGrammar == null) {
        createGrammarFrom(srcGrammar);
        return;
    }
    SchemaGrammar tmpGrammar = dstGrammar;
    if (tmpGrammar.isImmutable()) {
        tmpGrammar = createGrammarFrom(dstGrammar);
    }
    // add any new locations  
    addNewGrammarLocations(srcGrammar, tmpGrammar);
    // add any new imported grammars  
    addNewImportedGrammars(srcGrammar, tmpGrammar);
    // add any new global components  
    addNewGrammarComponents(srcGrammar, tmpGrammar);
}
