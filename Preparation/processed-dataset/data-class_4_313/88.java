private void addImportList(SchemaGrammar sg, Vector importedGrammars, Vector namespaceList) {
    final int size = namespaceList.size();
    SchemaGrammar isg;
    for (int i = 0; i < size; i++) {
        isg = fGrammarBucket.getGrammar((String) namespaceList.elementAt(i));
        if (isg != null) {
            importedGrammars.add(isg);
        } else {
        }
    }
}
