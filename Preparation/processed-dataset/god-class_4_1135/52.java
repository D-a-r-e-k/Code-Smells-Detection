/** 
     * Does a recursive (if necessary) check on the specified element's
     * content spec to make sure that all children refer to declared
     * elements.
     */
private void checkDeclaredElements(DTDGrammar grammar, int elementIndex, int contentSpecIndex, XMLContentSpec contentSpec) {
    grammar.getContentSpec(contentSpecIndex, contentSpec);
    if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
        String value = (String) contentSpec.value;
        if (value != null && grammar.getElementDeclIndex(value) == -1) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "UndeclaredElementInContentSpec", new Object[] { grammar.getElementDeclName(elementIndex).rawname, value }, XMLErrorReporter.SEVERITY_WARNING);
        }
    } else if ((contentSpec.type == XMLContentSpec.CONTENTSPECNODE_CHOICE) || (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_SEQ)) {
        final int leftNode = ((int[]) contentSpec.value)[0];
        final int rightNode = ((int[]) contentSpec.otherValue)[0];
        //  Recurse on both children.  
        checkDeclaredElements(grammar, elementIndex, leftNode, contentSpec);
        checkDeclaredElements(grammar, elementIndex, rightNode, contentSpec);
    } else if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE || contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE || contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE) {
        final int leftNode = ((int[]) contentSpec.value)[0];
        checkDeclaredElements(grammar, elementIndex, leftNode, contentSpec);
    }
}
