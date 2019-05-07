/**
     * Extract location information from an Element node, store such
     * information in the passed-in SimpleLocator object, then return
     * true. Returning false means can't extract or store such information.
     */
public boolean element2Locator(Element e, SimpleLocator l) {
    if (l == null)
        return false;
    if (e instanceof ElementImpl) {
        ElementImpl ele = (ElementImpl) e;
        // get system id from document object  
        Document doc = ele.getOwnerDocument();
        String sid = (String) fDoc2SystemId.get(DOMUtil.getRoot(doc));
        // line/column numbers are stored in the element node  
        int line = ele.getLineNumber();
        int column = ele.getColumnNumber();
        l.setValues(sid, sid, line, column, ele.getCharacterOffset());
        return true;
    }
    return false;
}
