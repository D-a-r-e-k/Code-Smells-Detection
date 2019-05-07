// getRecognizedProperties():String[]  
/**
     * Sets the value of a property. This method is called by the component
     * manager any time after reset when a property changes value.
     * <p>
     * <strong>Note:</strong> Components should silently ignore properties
     * that do not affect the operation of the component.
     *
     * @param propertyId The property identifier.
     * @param value      The value of the property.
     *
     * @throws SAXNotRecognizedException The component should not throw
     *                                   this exception.
     * @throws SAXNotSupportedException The component should not throw
     *                                  this exception.
     */
public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
    // Xerces properties  
    if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
        final int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
        if (suffixLength == Constants.SYMBOL_TABLE_PROPERTY.length() && propertyId.endsWith(Constants.SYMBOL_TABLE_PROPERTY)) {
            fSymbolTable = (SymbolTable) value;
            return;
        }
        if (suffixLength == Constants.ERROR_REPORTER_PROPERTY.length() && propertyId.endsWith(Constants.ERROR_REPORTER_PROPERTY)) {
            fErrorReporter = (XMLErrorReporter) value;
            return;
        }
        if (suffixLength == Constants.ENTITY_RESOLVER_PROPERTY.length() && propertyId.endsWith(Constants.ENTITY_RESOLVER_PROPERTY)) {
            fEntityResolver = (XMLEntityResolver) value;
            return;
        }
        if (suffixLength == Constants.BUFFER_SIZE_PROPERTY.length() && propertyId.endsWith(Constants.BUFFER_SIZE_PROPERTY)) {
            Integer bufferSize = (Integer) value;
            if (bufferSize != null && bufferSize.intValue() > DEFAULT_XMLDECL_BUFFER_SIZE) {
                fBufferSize = bufferSize.intValue();
                fEntityScanner.setBufferSize(fBufferSize);
                fSmallByteBufferPool.setBufferSize(fBufferSize);
                fLargeByteBufferPool.setBufferSize(fBufferSize << 1);
                fCharacterBufferPool.setExternalBufferSize(fBufferSize);
            }
        }
        if (suffixLength == Constants.SECURITY_MANAGER_PROPERTY.length() && propertyId.endsWith(Constants.SECURITY_MANAGER_PROPERTY)) {
            fSecurityManager = (SecurityManager) value;
            fEntityExpansionLimit = (fSecurityManager != null) ? fSecurityManager.getEntityExpansionLimit() : 0;
        }
    }
}
