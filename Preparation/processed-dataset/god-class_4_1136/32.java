// addDTDDefaultAttrsAndValidate(int,XMLAttrList)  
/** Checks entities in attribute values for standalone VC. */
protected String getExternalEntityRefInAttrValue(String nonNormalizedValue) {
    int valLength = nonNormalizedValue.length();
    int ampIndex = nonNormalizedValue.indexOf('&');
    while (ampIndex != -1) {
        if (ampIndex + 1 < valLength && nonNormalizedValue.charAt(ampIndex + 1) != '#') {
            int semicolonIndex = nonNormalizedValue.indexOf(';', ampIndex + 1);
            String entityName = nonNormalizedValue.substring(ampIndex + 1, semicolonIndex);
            entityName = fSymbolTable.addSymbol(entityName);
            int entIndex = fDTDGrammar.getEntityDeclIndex(entityName);
            if (entIndex > -1) {
                fDTDGrammar.getEntityDecl(entIndex, fEntityDecl);
                if (fEntityDecl.inExternal || (entityName = getExternalEntityRefInAttrValue(fEntityDecl.value)) != null) {
                    return entityName;
                }
            }
        }
        ampIndex = nonNormalizedValue.indexOf('&', ampIndex + 1);
    }
    return null;
}
