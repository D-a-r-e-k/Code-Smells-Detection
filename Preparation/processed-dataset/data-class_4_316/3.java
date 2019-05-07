/** DOM L3-EXPERIMENTAL:
     * Check if parameter can be set
     */
public boolean canSetParameter(String name, Object state) {
    if (state == null) {
        return true;
    }
    if (state instanceof Boolean) {
        boolean value = ((Boolean) state).booleanValue();
        if (name.equalsIgnoreCase(Constants.DOM_NAMESPACES) || name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA) || name.equalsIgnoreCase(Constants.DOM_DISCARD_DEFAULT_CONTENT) || name.equalsIgnoreCase(Constants.DOM_XMLDECL) || name.equalsIgnoreCase(Constants.DOM_WELLFORMED) || name.equalsIgnoreCase(Constants.DOM_INFOSET) || name.equalsIgnoreCase(Constants.DOM_ENTITIES) || name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS) || name.equalsIgnoreCase(Constants.DOM_COMMENTS) || name.equalsIgnoreCase(Constants.DOM_FORMAT_PRETTY_PRINT) || name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
            // both values supported  
            return true;
        } else if (name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_VALIDATE) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS)) {
            // true is not supported  
            return !value;
        } else if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
            // false is not supported  
            return value;
        }
    } else if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER) && state == null || state instanceof DOMErrorHandler) {
        return true;
    }
    return false;
}
