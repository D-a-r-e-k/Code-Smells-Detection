private boolean existingGrammars(Vector grammars) {
    int length = grammars.size();
    final XSDDescription desc = new XSDDescription();
    for (int i = 0; i < length; i++) {
        final SchemaGrammar sg1 = (SchemaGrammar) grammars.elementAt(i);
        desc.setNamespace(sg1.getTargetNamespace());
        final SchemaGrammar sg2 = findGrammar(desc, false);
        if (sg2 != null) {
            return true;
        }
    }
    return false;
}
