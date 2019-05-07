// rootElementSpecified(QName)  
/**
     * Check that the content of an element is valid.
     * <p>
     * This is the method of primary concern to the validator. This method is called
     * upon the scanner reaching the end tag of an element. At that time, the
     * element's children must be structurally validated, so it calls this method.
     * The index of the element being checked (in the decl pool), is provided as
     * well as an array of element name indexes of the children. The validator must
     * confirm that this element can have these children in this order.
     * <p>
     * This can also be called to do 'what if' testing of content models just to see
     * if they would be valid.
     * <p>
     * Note that the element index is an index into the element decl pool, whereas
     * the children indexes are name indexes, i.e. into the string pool.
     * <p>
     * A value of -1 in the children array indicates a PCDATA node. All other
     * indexes will be positive and represent child elements. The count can be
     * zero, since some elements have the EMPTY content model and that must be
     * confirmed.
     *
     * @param elementIndex The index within the <code>ElementDeclPool</code> of this
     *                     element.
     * @param childCount The number of entries in the <code>children</code> array.
     * @param children The children of this element.  
     *
     * @return The value -1 if fully valid, else the 0 based index of the child
     *         that first failed. If the value returned is equal to the number
     *         of children, then additional content is required to reach a valid
     *         ending state.
     *
     * @exception Exception Thrown on error.
     */
private int checkContent(int elementIndex, QName[] children, int childOffset, int childCount) throws XNIException {
    fDTDGrammar.getElementDecl(elementIndex, fTempElementDecl);
    // Get the element name index from the element  
    final String elementType = fCurrentElement.rawname;
    // Get out the content spec for this element  
    final int contentType = fCurrentContentSpecType;
    //  
    //  Deal with the possible types of content. We try to optimized here  
    //  by dealing specially with content models that don't require the  
    //  full DFA treatment.  
    //  
    if (contentType == XMLElementDecl.TYPE_EMPTY) {
        //  
        //  If the child count is greater than zero, then this is  
        //  an error right off the bat at index 0.  
        //  
        if (childCount != 0) {
            return 0;
        }
    } else if (contentType == XMLElementDecl.TYPE_ANY) {
    } else if (contentType == XMLElementDecl.TYPE_MIXED || contentType == XMLElementDecl.TYPE_CHILDREN) {
        // Get the content model for this element, faulting it in if needed  
        ContentModelValidator cmElem = null;
        cmElem = fTempElementDecl.contentModelValidator;
        int result = cmElem.validate(children, childOffset, childCount);
        return result;
    } else if (contentType == -1) {
    } else if (contentType == XMLElementDecl.TYPE_SIMPLE) {
    } else {
    }
    // We succeeded  
    return -1;
}
