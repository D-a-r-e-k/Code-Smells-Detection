/**
     * Expands a system id and returns the system id as a URI, if
     * it can be expanded. A return value of null means that the
     * identifier is already expanded. An exception thrown
     * indicates a failure to expand the id.
     *
     * @param systemId The systemId to be expanded.
     *
     * @return Returns the URI string representing the expanded system
     *         identifier. A null value indicates that the given
     *         system identifier is already expanded.
     *
     */
public static String expandSystemId(String systemId, String baseSystemId, boolean strict) throws URI.MalformedURIException {
    // check if there is a system id before   
    // trying to expand it.  
    if (systemId == null) {
        return null;
    }
    // system id has to be a valid URI  
    if (strict) {
        return expandSystemIdStrictOn(systemId, baseSystemId);
    }
    // Assume the URIs are well-formed. If it turns out they're not, try fixing them up.  
    try {
        return expandSystemIdStrictOff(systemId, baseSystemId);
    } catch (URI.MalformedURIException e) {
    }
    // check for bad parameters id  
    if (systemId.length() == 0) {
        return systemId;
    }
    // normalize id  
    String id = fixURI(systemId);
    // normalize base  
    URI base = null;
    URI uri = null;
    try {
        if (baseSystemId == null || baseSystemId.length() == 0 || baseSystemId.equals(systemId)) {
            base = getUserDir();
        } else {
            try {
                base = new URI(fixURI(baseSystemId).trim());
            } catch (URI.MalformedURIException e) {
                if (baseSystemId.indexOf(':') != -1) {
                    // for xml schemas we might have baseURI with  
                    // a specified drive  
                    base = new URI("file", "", fixURI(baseSystemId).trim(), null, null);
                } else {
                    base = new URI(getUserDir(), fixURI(baseSystemId));
                }
            }
        }
        // expand id  
        uri = new URI(base, id.trim());
    } catch (Exception e) {
    }
    if (uri == null) {
        return systemId;
    }
    return uri.toString();
}
