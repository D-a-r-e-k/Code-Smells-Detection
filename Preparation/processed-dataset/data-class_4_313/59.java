private void addNewImportedGrammars(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    final Vector igs1 = srcGrammar.getImportedGrammars();
    if (igs1 != null) {
        Vector igs2 = dstGrammar.getImportedGrammars();
        if (igs2 == null) {
            igs2 = ((Vector) igs1.clone());
            dstGrammar.setImportedGrammars(igs2);
        } else {
            updateImportList(igs1, igs2);
        }
    }
}
