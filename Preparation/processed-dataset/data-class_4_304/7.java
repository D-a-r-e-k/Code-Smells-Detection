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
    super.setProperty(propertyId, value);
    // Xerces properties  
    if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
        final int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
        if (suffixLength == Constants.ENTITY_MANAGER_PROPERTY.length() && propertyId.endsWith(Constants.ENTITY_MANAGER_PROPERTY)) {
            fEntityManager = (XMLEntityManager) value;
            return;
        }
        if (suffixLength == Constants.ENTITY_RESOLVER_PROPERTY.length() && propertyId.endsWith(Constants.ENTITY_RESOLVER_PROPERTY)) {
            fExternalSubsetResolver = (value instanceof ExternalSubsetResolver) ? (ExternalSubsetResolver) value : null;
            return;
        }
    }
}
