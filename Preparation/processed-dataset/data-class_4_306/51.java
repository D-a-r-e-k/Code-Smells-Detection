// isValidName(String):  boolean  
/** 
     * Checks that all elements referenced in content models have
     * been declared. This method calls out to the error handler 
     * to indicate warnings.
     */
private void checkDeclaredElements(DTDGrammar grammar) {
    int elementIndex = grammar.getFirstElementDeclIndex();
    XMLContentSpec contentSpec = new XMLContentSpec();
    while (elementIndex >= 0) {
        int type = grammar.getContentSpecType(elementIndex);
        if (type == XMLElementDecl.TYPE_CHILDREN || type == XMLElementDecl.TYPE_MIXED) {
            checkDeclaredElements(grammar, elementIndex, grammar.getContentSpecIndex(elementIndex), contentSpec);
        }
        elementIndex = grammar.getNextElementDeclIndex(elementIndex);
    }
}
