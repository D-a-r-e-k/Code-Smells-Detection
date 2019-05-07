/**
     * Extracts all the required non-cookie headers for that particular URL request and
     * sets them in the <code>HttpMethod</code> passed in
     *
     * @param method
     *            <code>HttpMethod</code> which represents the request
     * @param u
     *            <code>URL</code> of the URL request
     * @param headerManager
     *            the <code>HeaderManager</code> containing all the cookies
     *            for this <code>UrlConfig</code>
     * @param cacheManager the CacheManager (may be null)
     */
private void setConnectionHeaders(HttpMethod method, URL u, HeaderManager headerManager, CacheManager cacheManager) {
    // Set all the headers from the HeaderManager 
    if (headerManager != null) {
        CollectionProperty headers = headerManager.getHeaders();
        if (headers != null) {
            PropertyIterator i = headers.iterator();
            while (i.hasNext()) {
                org.apache.jmeter.protocol.http.control.Header header = (org.apache.jmeter.protocol.http.control.Header) i.next().getObjectValue();
                String n = header.getName();
                // Don't allow override of Content-Length 
                // This helps with SoapSampler hack too 
                // TODO - what other headers are not allowed? 
                if (!HTTPConstants.HEADER_CONTENT_LENGTH.equalsIgnoreCase(n)) {
                    String v = header.getValue();
                    if (HTTPConstants.HEADER_HOST.equalsIgnoreCase(n)) {
                        v = v.replaceFirst(":\\d+$", "");
                        // remove any port specification // $NON-NLS-1$ $NON-NLS-2$ 
                        method.getParams().setVirtualHost(v);
                    } else {
                        method.addRequestHeader(n, v);
                    }
                }
            }
        }
    }
    if (cacheManager != null) {
        cacheManager.setHeaders(u, method);
    }
}
