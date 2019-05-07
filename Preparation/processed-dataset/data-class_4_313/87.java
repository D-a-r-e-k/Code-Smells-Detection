private void expandImportList(String namespace, Vector namespaceList) {
    SchemaGrammar sg = fGrammarBucket.getGrammar(namespace);
    // shouldn't be null  
    if (sg != null) {
        Vector isgs = sg.getImportedGrammars();
        if (isgs == null) {
            isgs = new Vector();
            addImportList(sg, isgs, namespaceList);
            sg.setImportedGrammars(isgs);
        } else {
            updateImportList(sg, isgs, namespaceList);
        }
    }
}
