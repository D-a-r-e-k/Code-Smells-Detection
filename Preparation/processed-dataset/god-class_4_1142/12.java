/**
     * Namespace growth
     * 
     * Go throuth the grammar bucket, and for each grammar in the bucket
     * check the import list. If there exists a grammar in import list
     * that has the same namespace as newGrammar, but a different instance,
     * then update the import list and replace the old grammar instance with
     * the new one
     */
private void updateImportListWith(SchemaGrammar newGrammar) {
    SchemaGrammar[] schemaGrammars = fGrammarBucket.getGrammars();
    for (int i = 0; i < schemaGrammars.length; ++i) {
        SchemaGrammar sg = schemaGrammars[i];
        if (sg != newGrammar) {
            Vector importedGrammars = sg.getImportedGrammars();
            if (importedGrammars != null) {
                for (int j = 0; j < importedGrammars.size(); j++) {
                    SchemaGrammar isg = (SchemaGrammar) importedGrammars.elementAt(j);
                    if (null2EmptyString(isg.getTargetNamespace()).equals(null2EmptyString(newGrammar.getTargetNamespace()))) {
                        if (isg != newGrammar) {
                            importedGrammars.set(j, newGrammar);
                        }
                        break;
                    }
                }
            }
        }
    }
}
