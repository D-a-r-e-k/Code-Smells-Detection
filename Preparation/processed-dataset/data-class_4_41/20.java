// setFeature(String,boolean) 
/** Sets a property. */
public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
    if (propertyId.equals(NAMES_ELEMS)) {
        fNamesElems = getNamesValue(String.valueOf(value));
        return;
    }
    if (propertyId.equals(NAMES_ATTRS)) {
        fNamesAttrs = getNamesValue(String.valueOf(value));
        return;
    }
    if (propertyId.equals(DEFAULT_ENCODING)) {
        fDefaultIANAEncoding = String.valueOf(value);
        return;
    }
}
