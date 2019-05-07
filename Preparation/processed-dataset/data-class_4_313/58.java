private void addNewGrammarLocations(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    final StringList locations = srcGrammar.getDocumentLocations();
    final int locSize = locations.size();
    final StringList locations2 = dstGrammar.getDocumentLocations();
    for (int i = 0; i < locSize; i++) {
        String loc = locations.item(i);
        if (!locations2.contains(loc)) {
            dstGrammar.addDocument(null, loc);
        }
    }
}
