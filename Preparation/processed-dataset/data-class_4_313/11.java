/**
     * Namespace growth
     * 
     * Go through the import list of a given grammar and for each imported
     * grammar, check to see if the grammar bucket has a newer version.
     * If a new instance is found, we update the import list with the
     * newer version.
     */
private void updateImportListFor(SchemaGrammar grammar) {
    Vector importedGrammars = grammar.getImportedGrammars();
    if (importedGrammars != null) {
        for (int i = 0; i < importedGrammars.size(); i++) {
            SchemaGrammar isg1 = (SchemaGrammar) importedGrammars.elementAt(i);
            SchemaGrammar isg2 = fGrammarBucket.getGrammar(isg1.getTargetNamespace());
            if (isg2 != null && isg1 != isg2) {
                importedGrammars.set(i, isg2);
            }
        }
    }
}
