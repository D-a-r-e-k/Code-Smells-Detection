// getValue(XMLAttributes,String):String 
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
public static String expandSystemId(String systemId, String baseSystemId) {
    // check for bad parameters id 
    if (systemId == null || systemId.length() == 0) {
        return systemId;
    }
    // if id already expanded, return 
    try {
        URI uri = new URI(systemId);
        if (uri != null) {
            return systemId;
        }
    } catch (URI.MalformedURIException e) {
    }
    // normalize id 
    String id = fixURI(systemId);
    // normalize base 
    URI base = null;
    URI uri = null;
    try {
        if (baseSystemId == null || baseSystemId.length() == 0 || baseSystemId.equals(systemId)) {
            String dir;
            try {
                dir = fixURI(System.getProperty("user.dir"));
            } catch (SecurityException se) {
                dir = "";
            }
            if (!dir.endsWith("/")) {
                dir = dir + "/";
            }
            base = new URI("file", "", dir, null, null);
        } else {
            try {
                base = new URI(fixURI(baseSystemId));
            } catch (URI.MalformedURIException e) {
                String dir;
                try {
                    dir = fixURI(System.getProperty("user.dir"));
                } catch (SecurityException se) {
                    dir = "";
                }
                if (baseSystemId.indexOf(':') != -1) {
                    // for xml schemas we might have baseURI with 
                    // a specified drive 
                    base = new URI("file", "", fixURI(baseSystemId), null, null);
                } else {
                    if (!dir.endsWith("/")) {
                        dir = dir + "/";
                    }
                    dir = dir + fixURI(baseSystemId);
                    base = new URI("file", "", dir, null, null);
                }
            }
        }
        // expand id 
        uri = new URI(base, id);
    } catch (URI.MalformedURIException e) {
    }
    if (uri == null) {
        return systemId;
    }
    return uri.toString();
}
