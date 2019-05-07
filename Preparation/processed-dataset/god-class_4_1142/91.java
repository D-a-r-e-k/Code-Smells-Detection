// NOTE: always assuming that fNamespaceGrowth is enabled  
//       otherwise the grammar should have existed  
private SchemaGrammar getSchemaGrammar(XSDDescription desc) {
    SchemaGrammar sg = findGrammar(desc, fNamespaceGrowth);
    if (sg == null) {
        sg = new SchemaGrammar(desc.getNamespace(), desc.makeClone(), fSymbolTable);
        fGrammarBucket.putGrammar(sg);
    } else if (sg.isImmutable()) {
        sg = createGrammarFrom(sg);
    }
    return sg;
}
