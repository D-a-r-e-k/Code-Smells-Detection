// renameRedefiningComponents(XSDocumentInfo, Element, String, String, String):void  
// this method takes a name of the form a:b, determines the URI mapped  
// to by a in the current SchemaNamespaceSupport object, and returns this  
// information in the form (nsURI,b) suitable for lookups in the global  
// decl Hashtables.  
// REVISIT: should have it return QName, instead of String. this would  
//          save lots of string concatenation time. we can use  
//          QName#equals() to compare two QNames, and use QName directly  
//          as a key to the SymbolHash.  
//          And when the DV's are ready to return compiled values from  
//          validate() method, we should just call QNameDV.validate()  
//          in this method.  
private String findQName(String name, XSDocumentInfo schemaDoc) {
    SchemaNamespaceSupport currNSMap = schemaDoc.fNamespaceSupport;
    int colonPtr = name.indexOf(':');
    String prefix = XMLSymbols.EMPTY_STRING;
    if (colonPtr > 0)
        prefix = name.substring(0, colonPtr);
    String uri = currNSMap.getURI(fSymbolTable.addSymbol(prefix));
    String localpart = (colonPtr == 0) ? name : name.substring(colonPtr + 1);
    if (prefix == XMLSymbols.EMPTY_STRING && uri == null && schemaDoc.fIsChameleonSchema)
        uri = schemaDoc.fTargetNamespace;
    if (uri == null)
        return "," + localpart;
    return uri + "," + localpart;
}
