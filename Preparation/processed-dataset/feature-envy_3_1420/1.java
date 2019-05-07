/**
     * Helper to convert the host name portion of a URI to its corresponding IP
     * address. If the URI doesn't contain a host, or the host is specified as
     * an IP address, or the IP address can't be determined, then the URI is
     * returned unchanged.
     *
     * @param uri the uri to convert
     * @return the converted URI
     */
public static URI convertHostToAddress(URI uri) {
    URI result = uri;
    String host = uri.getHost();
    if (host != null && !host.equals("")) {
        try {
            InetAddress address = InetAddress.getByName(host);
            result = new URI(uri.getScheme(), uri.getUserinfo(), address.getHostAddress(), uri.getPort(), uri.getPath(), uri.getQueryString(), uri.getFragment());
            fixPath(result);
        } catch (UnknownHostException ignore) {
        } catch (URI.MalformedURIException exception) {
            // should *never* happen 
            throw new IllegalArgumentException("Failed to construct URI: " + exception.getMessage());
        }
    }
    return result;
}
