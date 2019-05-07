// $NON-NLS-1$ 
/**
     * Get the URL, built from its component parts.
     *
     * <p>
     * As a special case, if the path starts with "http[s]://",
     * then the path is assumed to be the entire URL.
     * </p>
     *
     * @return The URL to be requested by this sampler.
     * @throws MalformedURLException
     */
public URL getUrl() throws MalformedURLException {
    StringBuilder pathAndQuery = new StringBuilder(100);
    String path = this.getPath();
    // Hack to allow entire URL to be provided in host field 
    if (path.startsWith(HTTP_PREFIX) || path.startsWith(HTTPS_PREFIX)) {
        return new URL(path);
    }
    String domain = getDomain();
    String protocol = getProtocol();
    if (PROTOCOL_FILE.equalsIgnoreCase(protocol)) {
        domain = null;
    } else {
        // HTTP URLs must be absolute, allow file to be relative 
        if (!path.startsWith("/")) {
            // $NON-NLS-1$ 
            pathAndQuery.append("/");
        }
    }
    pathAndQuery.append(path);
    // Add the query string if it is a HTTP GET or DELETE request 
    if (HTTPConstants.GET.equals(getMethod()) || HTTPConstants.DELETE.equals(getMethod())) {
        // Get the query string encoded in specified encoding 
        // If no encoding is specified by user, we will get it 
        // encoded in UTF-8, which is what the HTTP spec says 
        String queryString = getQueryString(getContentEncoding());
        if (queryString.length() > 0) {
            if (path.indexOf(QRY_PFX) > -1) {
                // Already contains a prefix 
                pathAndQuery.append(QRY_SEP);
            } else {
                pathAndQuery.append(QRY_PFX);
            }
            pathAndQuery.append(queryString);
        }
    }
    // If default port for protocol is used, we do not include port in URL 
    if (isProtocolDefaultPort()) {
        return new URL(protocol, domain, pathAndQuery.toString());
    }
    return new URL(protocol, domain, getPort(), pathAndQuery.toString());
}
