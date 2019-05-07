/**
     * Sets the PATH property; if the request is a GET or DELETE (and the path
     * does not start with http[s]://) it also calls {@link #parseArguments(String, String)}
     * to extract and store any query arguments.
     *
     * @param path
     *            The new Path value
     * @param contentEncoding
     *            The encoding used for the querystring parameter values
     */
public void setPath(String path, String contentEncoding) {
    boolean fullUrl = path.startsWith(HTTP_PREFIX) || path.startsWith(HTTPS_PREFIX);
    if (!fullUrl && (HTTPConstants.GET.equals(getMethod()) || HTTPConstants.DELETE.equals(getMethod()))) {
        int index = path.indexOf(QRY_PFX);
        if (index > -1) {
            setProperty(PATH, path.substring(0, index));
            // Parse the arguments in querystring, assuming specified encoding for values 
            parseArguments(path.substring(index + 1), contentEncoding);
        } else {
            setProperty(PATH, path);
        }
    } else {
        setProperty(PATH, path);
    }
}
