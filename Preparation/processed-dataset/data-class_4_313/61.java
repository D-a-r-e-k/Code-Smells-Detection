private void addNewGrammarComponents(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    dstGrammar.resetComponents();
    addGlobalElementDecls(srcGrammar, dstGrammar);
    addGlobalAttributeDecls(srcGrammar, dstGrammar);
    addGlobalAttributeGroupDecls(srcGrammar, dstGrammar);
    addGlobalGroupDecls(srcGrammar, dstGrammar);
    addGlobalTypeDecls(srcGrammar, dstGrammar);
    addGlobalNotationDecls(srcGrammar, dstGrammar);
}
