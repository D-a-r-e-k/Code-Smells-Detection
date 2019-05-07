public void resolveNamespace(Element element, Attr[] attrs, SchemaNamespaceSupport nsSupport) {
    // push the namespace context  
    nsSupport.pushContext();
    // search for new namespace bindings  
    int length = attrs.length;
    Attr sattr = null;
    String rawname, prefix, uri;
    for (int i = 0; i < length; i++) {
        sattr = attrs[i];
        rawname = DOMUtil.getName(sattr);
        prefix = null;
        if (rawname.equals(XMLSymbols.PREFIX_XMLNS))
            prefix = XMLSymbols.EMPTY_STRING;
        else if (rawname.startsWith("xmlns:"))
            prefix = fSymbolTable.addSymbol(DOMUtil.getLocalName(sattr));
        if (prefix != null) {
            uri = fSymbolTable.addSymbol(DOMUtil.getValue(sattr));
            nsSupport.declarePrefix(prefix, uri.length() != 0 ? uri : null);
        }
    }
}
