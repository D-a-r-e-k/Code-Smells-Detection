/**
     * Gets the ResponseHeaders
     *
     * @param method the method used to perform the request
     * @return string containing the headers, one per line
     */
protected String getResponseHeaders(HttpMethod method) {
    StringBuilder headerBuf = new StringBuilder();
    org.apache.commons.httpclient.Header rh[] = method.getResponseHeaders();
    headerBuf.append(method.getStatusLine());
    // header[0] is not the status line... 
    headerBuf.append("\n");
    // $NON-NLS-1$ 
    for (int i = 0; i < rh.length; i++) {
        String key = rh[i].getName();
        headerBuf.append(key);
        headerBuf.append(": ");
        // $NON-NLS-1$ 
        headerBuf.append(rh[i].getValue());
        headerBuf.append("\n");
    }
    return headerBuf.toString();
}
