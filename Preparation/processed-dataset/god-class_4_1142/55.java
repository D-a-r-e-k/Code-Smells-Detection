private void addGrammars(Vector grammars) {
    int length = grammars.size();
    XSDDescription desc = new XSDDescription();
    for (int i = 0; i < length; i++) {
        final SchemaGrammar sg1 = (SchemaGrammar) grammars.elementAt(i);
        desc.setNamespace(sg1.getTargetNamespace());
        final SchemaGrammar sg2 = findGrammar(desc, fNamespaceGrowth);
        if (sg1 != sg2) {
            addGrammarComponents(sg1, sg2);
        }
    }
}
