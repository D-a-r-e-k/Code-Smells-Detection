/**
     * Absolutizes a URI using the current value
     * of the "user.dir" property as the base URI. If
     * the URI is already absolute, this is a no-op.
     * 
     * @param uri the URI to absolutize
     */
public static void absolutizeAgainstUserDir(URI uri) throws URI.MalformedURIException {
    uri.absolutize(getUserDir());
}
