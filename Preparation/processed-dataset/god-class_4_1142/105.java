// checkForDuplicateNames(String, Hashtable, Element, XSDocumentInfo):void  
void checkForDuplicateNames(String qName, int declType, Element currComp) {
    int namespaceEnd = qName.indexOf(',');
    String namespace = qName.substring(0, namespaceEnd);
    SchemaGrammar grammar = fGrammarBucket.getGrammar(emptyString2Null(namespace));
    if (grammar != null) {
        Object obj = getGlobalDeclFromGrammar(grammar, declType, qName.substring(namespaceEnd + 1));
        if (obj != null) {
            reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
        }
    }
}
