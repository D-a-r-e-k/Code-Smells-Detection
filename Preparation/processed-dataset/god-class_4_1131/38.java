// expandSystemIdStrictOn(String,String):String  
/**
     * Helper method for expandSystemId(String,String,boolean):String
     */
private static String expandSystemIdStrictOff(String systemId, String baseSystemId) throws URI.MalformedURIException {
    URI systemURI = new URI(systemId, true);
    // If it's already an absolute one, return it  
    if (systemURI.isAbsoluteURI()) {
        if (systemURI.getScheme().length() > 1) {
            return systemId;
        }
        /** 
             * If the scheme's length is only one character,
             * it's likely that this was intended as a file
             * path. Fixing this up in expandSystemId to
             * maintain backwards compatibility.
             */
        throw new URI.MalformedURIException();
    }
    // If there isn't a base URI, use the working directory  
    URI baseURI = null;
    if (baseSystemId == null || baseSystemId.length() == 0) {
        baseURI = getUserDir();
    } else {
        baseURI = new URI(baseSystemId, true);
        if (!baseURI.isAbsoluteURI()) {
            // assume "base" is also a relative uri  
            baseURI.absolutize(getUserDir());
        }
    }
    // absolutize the system identifier using the base URI  
    systemURI.absolutize(baseURI);
    // return the string rep of the new uri (an absolute one)  
    return systemURI.toString();
}
