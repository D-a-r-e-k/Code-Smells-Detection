/**
     * Get all the request headers for the <code>HttpMethod</code>
     *
     * @param method
     *            <code>HttpMethod</code> which represents the request
     * @return the headers as a string
     */
protected String getConnectionHeaders(HttpMethod method) {
    // Get all the request headers 
    StringBuilder hdrs = new StringBuilder(100);
    Header[] requestHeaders = method.getRequestHeaders();
    for (int i = 0; i < requestHeaders.length; i++) {
        // Exclude the COOKIE header, since cookie is reported separately in the sample 
        if (!HTTPConstants.HEADER_COOKIE.equalsIgnoreCase(requestHeaders[i].getName())) {
            hdrs.append(requestHeaders[i].getName());
            hdrs.append(": ");
            // $NON-NLS-1$ 
            hdrs.append(requestHeaders[i].getValue());
            hdrs.append("\n");
        }
    }
    return hdrs.toString();
}
