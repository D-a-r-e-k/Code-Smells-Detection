/**
     * Encode a string using algorithm specified in web.xml and return the
     * resulting encrypted password. If exception, the plain credentials
     * string is returned
     *
     * @param password Password or other credentials to use in authenticating
     *        this username
     * @param algorithm Algorithm used to do the digest
     *
     * @return encypted password based on the algorithm.
     */
public static String encodePassword(String password, String algorithm) {
    byte[] unencodedPassword = password.getBytes();
    MessageDigest md = null;
    try {
        // first create an instance, given the provider 
        md = MessageDigest.getInstance(algorithm);
    } catch (Exception e) {
        mLogger.error("Exception: " + e);
        return password;
    }
    md.reset();
    // call the update method one or more times 
    // (useful when you don't know the size of your data, eg. stream) 
    md.update(unencodedPassword);
    // now calculate the hash 
    byte[] encodedPassword = md.digest();
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < encodedPassword.length; i++) {
        if ((encodedPassword[i] & 0xff) < 0x10) {
            buf.append("0");
        }
        buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
    }
    return buf.toString();
}
