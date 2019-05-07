// reset(XMLComponentManager)  
/**
     * Sets the value of a property during parsing.
     * 
     * @param propertyId 
     * @param value 
     */
public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
    // Xerces properties  
    if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
        final int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
        if (suffixLength == Constants.SYMBOL_TABLE_PROPERTY.length() && propertyId.endsWith(Constants.SYMBOL_TABLE_PROPERTY)) {
            fSymbolTable = (SymbolTable) value;
        } else if (suffixLength == Constants.ERROR_REPORTER_PROPERTY.length() && propertyId.endsWith(Constants.ERROR_REPORTER_PROPERTY)) {
            fErrorReporter = (XMLErrorReporter) value;
        } else if (suffixLength == Constants.ENTITY_MANAGER_PROPERTY.length() && propertyId.endsWith(Constants.ENTITY_MANAGER_PROPERTY)) {
            fEntityManager = (XMLEntityManager) value;
        }
    }
}
