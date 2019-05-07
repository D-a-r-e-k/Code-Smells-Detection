// expandSystemId(String,String,boolean):String  
/**
     * Helper method for expandSystemId(String,String,boolean):String
     */
private static String expandSystemIdStrictOn(String systemId, String baseSystemId) throws URI.MalformedURIException {
    URI systemURI = new URI(systemId, true);
    // If it's already an absolute one, return it  
    if (systemURI.isAbsoluteURI()) {
        return systemId;
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
