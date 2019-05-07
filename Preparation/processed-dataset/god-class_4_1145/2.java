/** DOM L3-EXPERIMENTAL:
     * Setter for boolean and object parameters
     */
public void setParameter(String name, Object value) throws DOMException {
    if (value instanceof Boolean) {
        boolean state = ((Boolean) value).booleanValue();
        if (name.equalsIgnoreCase(Constants.DOM_INFOSET)) {
            if (state) {
                features &= ~ENTITIES;
                features &= ~CDATA;
                features |= NAMESPACES;
                features |= NSDECL;
                features |= WELLFORMED;
                features |= COMMENTS;
            }
        } else if (name.equalsIgnoreCase(Constants.DOM_XMLDECL)) {
            features = (short) (state ? features | XMLDECL : features & ~XMLDECL);
        } else if (name.equalsIgnoreCase(Constants.DOM_NAMESPACES)) {
            features = (short) (state ? features | NAMESPACES : features & ~NAMESPACES);
            serializer.fNamespaces = state;
        } else if (name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA)) {
            features = (short) (state ? features | SPLITCDATA : features & ~SPLITCDATA);
        } else if (name.equalsIgnoreCase(Constants.DOM_DISCARD_DEFAULT_CONTENT)) {
            features = (short) (state ? features | DISCARDDEFAULT : features & ~DISCARDDEFAULT);
        } else if (name.equalsIgnoreCase(Constants.DOM_WELLFORMED)) {
            features = (short) (state ? features | WELLFORMED : features & ~WELLFORMED);
        } else if (name.equalsIgnoreCase(Constants.DOM_ENTITIES)) {
            features = (short) (state ? features | ENTITIES : features & ~ENTITIES);
        } else if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS)) {
            features = (short) (state ? features | CDATA : features & ~CDATA);
        } else if (name.equalsIgnoreCase(Constants.DOM_COMMENTS)) {
            features = (short) (state ? features | COMMENTS : features & ~COMMENTS);
        } else if (name.equalsIgnoreCase(Constants.DOM_FORMAT_PRETTY_PRINT)) {
            features = (short) (state ? features | PRETTY_PRINT : features & ~PRETTY_PRINT);
        } else if (name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_VALIDATE) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS)) {
            // true is not supported  
            if (state) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[] { name });
                throw new DOMException(DOMException.NOT_SUPPORTED_ERR, msg);
            }
        } else if (name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
            //namespace-declaration has effect only if namespaces is true  
            features = (short) (state ? features | NSDECL : features & ~NSDECL);
            serializer.fNamespacePrefixes = state;
        } else if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
            // false is not supported  
            if (!state) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[] { name });
                throw new DOMException(DOMException.NOT_SUPPORTED_ERR, msg);
            }
        } else {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[] { name });
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, msg);
        }
    } else if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
        if (value == null || value instanceof DOMErrorHandler) {
            fErrorHandler = (DOMErrorHandler) value;
        } else {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[] { name });
            throw new DOMException(DOMException.TYPE_MISMATCH_ERR, msg);
        }
    } else if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER) || name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION) || name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE) && value != null) {
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[] { name });
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, msg);
    } else {
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[] { name });
        throw new DOMException(DOMException.NOT_FOUND_ERR, msg);
    }
}
