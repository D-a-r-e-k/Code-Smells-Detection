private void updateImportList(Vector importedSrc, Vector importedDst) {
    final int size = importedSrc.size();
    for (int i = 0; i < size; i++) {
        final SchemaGrammar sg = (SchemaGrammar) importedSrc.elementAt(i);
        if (!containedImportedGrammar(importedDst, sg)) {
            importedDst.add(sg);
        }
    }
}
