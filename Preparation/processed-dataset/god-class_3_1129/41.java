/**
     * Get the port number for a URL, applying defaults if necessary.
     * (Called by CookieManager.)
     * @param protocol from {@link URL#getProtocol()}
     * @param port number from {@link URL#getPort()}
     * @return the default port for the protocol
     */
public static int getDefaultPort(String protocol, int port) {
    if (port == URL_UNSPECIFIED_PORT) {
        return protocol.equalsIgnoreCase(HTTPConstants.PROTOCOL_HTTP) ? HTTPConstants.DEFAULT_HTTP_PORT : protocol.equalsIgnoreCase(HTTPConstants.PROTOCOL_HTTPS) ? HTTPConstants.DEFAULT_HTTPS_PORT : port;
    }
    return port;
}
