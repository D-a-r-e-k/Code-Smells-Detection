/**
     * Check whether the specified element conforms to the attributes restriction
     * an array of attribute values is returned. the caller must call
     * <code>returnAttrArray</code> to return that array.
     *
     * @param element    which element to check
     * @param isGlobal   whether a child of &lt;schema&gt; or &lt;redefine&gt;
     * @param schemaDoc  the document where the element lives in
     * @return           an array containing attribute values
     */
public Object[] checkAttributes(Element element, boolean isGlobal, XSDocumentInfo schemaDoc) {
    return checkAttributes(element, isGlobal, schemaDoc, false);
}
