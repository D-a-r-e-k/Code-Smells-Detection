/**
     * Calculate response headers size
     * 
     * @return the size response headers (in bytes)
     */
private static int calculateHeadersSize(HttpMethodBase httpMethod) {
    int headerSize = httpMethod.getStatusLine().toString().length() + 2;
    // add a \r\n 
    Header[] rh = httpMethod.getResponseHeaders();
    for (int i = 0; i < rh.length; i++) {
        headerSize += rh[i].toString().length();
    }
    headerSize += 2;
    // last \r\n before response data 
    return headerSize;
}
