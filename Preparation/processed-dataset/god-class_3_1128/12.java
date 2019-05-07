/**
     * From the <code>HttpMethod</code>, store all the "set-cookie" key-pair
     * values in the cookieManager of the <code>UrlConfig</code>.
     *
     * @param method
     *            <code>HttpMethod</code> which represents the request
     * @param u
     *            <code>URL</code> of the URL request
     * @param cookieManager
     *            the <code>CookieManager</code> containing all the cookies
     */
protected void saveConnectionCookies(HttpMethod method, URL u, CookieManager cookieManager) {
    if (cookieManager != null) {
        Header hdr[] = method.getResponseHeaders(HTTPConstants.HEADER_SET_COOKIE);
        for (int i = 0; i < hdr.length; i++) {
            cookieManager.addCookieFromHeader(hdr[i].getValue(), u);
        }
    }
}
