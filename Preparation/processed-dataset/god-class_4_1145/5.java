/** DOM L3-EXPERIMENTAL:
     * Getter for boolean and object parameters
     */
public Object getParameter(String name) throws DOMException {
    if (name.equalsIgnoreCase(Constants.DOM_COMMENTS)) {
        return ((features & COMMENTS) != 0) ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_NAMESPACES)) {
        return (features & NAMESPACES) != 0 ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_XMLDECL)) {
        return (features & XMLDECL) != 0 ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS)) {
        return (features & CDATA) != 0 ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_ENTITIES)) {
        return (features & ENTITIES) != 0 ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA)) {
        return (features & SPLITCDATA) != 0 ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_WELLFORMED)) {
        return (features & WELLFORMED) != 0 ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
        return (features & NSDECL) != 0 ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
        return Boolean.TRUE;
    } else if (name.equalsIgnoreCase(Constants.DOM_DISCARD_DEFAULT_CONTENT)) {
        return ((features & DISCARDDEFAULT) != 0) ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_FORMAT_PRETTY_PRINT)) {
        return ((features & PRETTY_PRINT) != 0) ? Boolean.TRUE : Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_INFOSET)) {
        if ((features & ENTITIES) == 0 && (features & CDATA) == 0 && (features & NAMESPACES) != 0 && (features & NSDECL) != 0 && (features & WELLFORMED) != 0 && (features & COMMENTS) != 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS) || name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_VALIDATE) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION)) {
        return Boolean.FALSE;
    } else if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
        return fErrorHandler;
    } else if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER) || name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION) || name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE)) {
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[] { name });
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, msg);
    } else {
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[] { name });
        throw new DOMException(DOMException.NOT_FOUND_ERR, msg);
    }
}
