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
    if (propertyId.equals(ROOT_TYPE_DEF)) {
        if (value == null) {
            fRootTypeQName = null;
            fRootTypeDefinition = null;
        } else if (value instanceof javax.xml.namespace.QName) {
            fRootTypeQName = (javax.xml.namespace.QName) value;
            fRootTypeDefinition = null;
        } else {
            fRootTypeDefinition = (XSTypeDefinition) value;
            fRootTypeQName = null;
        }
    } else if (propertyId.equals(ROOT_ELEMENT_DECL)) {
        if (value == null) {
            fRootElementDeclQName = null;
            fRootElementDeclaration = null;
        } else if (value instanceof javax.xml.namespace.QName) {
            fRootElementDeclQName = (javax.xml.namespace.QName) value;
            fRootElementDeclaration = null;
        } else {
            fRootElementDeclaration = (XSElementDecl) value;
            fRootElementDeclQName = null;
        }
    }
}
