/**
     * Extracts all the required cookies for that particular URL request and
     * sets them in the <code>HttpMethod</code> passed in.
     *
     * @param method <code>HttpMethod</code> for the request
     * @param u <code>URL</code> of the request
     * @param cookieManager the <code>CookieManager</code> containing all the cookies
     * @return a String containing the cookie details (for the response)
     * May be null
     */
private String setConnectionCookie(HttpMethod method, URL u, CookieManager cookieManager) {
    String cookieHeader = null;
    if (cookieManager != null) {
        cookieHeader = cookieManager.getCookieHeaderForURL(u);
        if (cookieHeader != null) {
            method.setRequestHeader(HTTPConstants.HEADER_COOKIE, cookieHeader);
        }
    }
    return cookieHeader;
}
