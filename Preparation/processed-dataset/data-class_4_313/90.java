private boolean containedImportedGrammar(Vector importedGrammar, SchemaGrammar grammar) {
    final int size = importedGrammar.size();
    SchemaGrammar sg;
    for (int i = 0; i < size; i++) {
        sg = (SchemaGrammar) importedGrammar.elementAt(i);
        if (null2EmptyString(sg.getTargetNamespace()).equals(null2EmptyString(grammar.getTargetNamespace()))) {
            return true;
        }
    }
    return false;
}
