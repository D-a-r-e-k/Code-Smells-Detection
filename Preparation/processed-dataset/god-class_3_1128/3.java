/**
     * Returns an <code>HttpConnection</code> fully ready to attempt
     * connection. This means it sets the request method (GET or POST), headers,
     * cookies, and authorization for the URL request.
     * <p>
     * The request infos are saved into the sample result if one is provided.
     *
     * @param u
     *            <code>URL</code> of the URL request
     * @param httpMethod
     *            GET/PUT/HEAD etc
     * @param res
     *            sample result to save request infos to
     * @return <code>HttpConnection</code> ready for .connect
     * @exception IOException
     *                if an I/O Exception occurs
     */
protected HttpClient setupConnection(URL u, HttpMethodBase httpMethod, HTTPSampleResult res) throws IOException {
    String urlStr = u.toString();
    org.apache.commons.httpclient.URI uri = new org.apache.commons.httpclient.URI(urlStr, false);
    String schema = uri.getScheme();
    if ((schema == null) || (schema.length() == 0)) {
        schema = HTTPConstants.PROTOCOL_HTTP;
    }
    if (HTTPConstants.PROTOCOL_HTTPS.equalsIgnoreCase(schema)) {
        SSLManager.getInstance();
    }
    Protocol protocol = Protocol.getProtocol(schema);
    String host = uri.getHost();
    int port = uri.getPort();
    /*
         *  We use the HostConfiguration as the key to retrieve the HttpClient,
         *  so need to ensure that any items used in its equals/hashcode methods are
         *  not changed after use, i.e.:
         *  host, port, protocol, localAddress, proxy
         *
        */
    HostConfiguration hc = new HostConfiguration();
    hc.setHost(host, port, protocol);
    // All needed to ensure re-usablility 
    // Set up the local address if one exists 
    if (localAddress != null) {
        hc.setLocalAddress(localAddress);
    } else {
        final String ipSource = getIpSource();
        if (ipSource.length() > 0) {
            // Use special field ip source address (for pseudo 'ip spoofing') 
            InetAddress inetAddr = InetAddress.getByName(ipSource);
            hc.setLocalAddress(inetAddr);
        }
    }
    final String proxyHost = getProxyHost();
    final int proxyPort = getProxyPortInt();
    boolean useStaticProxy = isStaticProxy(host);
    boolean useDynamicProxy = isDynamicProxy(proxyHost, proxyPort);
    if (useDynamicProxy) {
        hc.setProxy(proxyHost, proxyPort);
        useStaticProxy = false;
    } else if (useStaticProxy) {
        if (log.isDebugEnabled()) {
            log.debug("Setting proxy: " + PROXY_HOST + ":" + PROXY_PORT);
        }
        hc.setProxy(PROXY_HOST, PROXY_PORT);
    }
    Map<HostConfiguration, HttpClient> map = httpClients.get();
    // N.B. HostConfiguration.equals() includes proxy settings in the compare. 
    HttpClient httpClient = map.get(hc);
    if (httpClient == null) {
        httpClient = new HttpClient(new SimpleHttpConnectionManager());
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(RETRY_COUNT, false));
        if (log.isDebugEnabled()) {
            log.debug("Created new HttpClient: @" + System.identityHashCode(httpClient));
        }
        httpClient.setHostConfiguration(hc);
        map.put(hc, httpClient);
    } else {
        if (log.isDebugEnabled()) {
            log.debug("Reusing the HttpClient: @" + System.identityHashCode(httpClient));
        }
    }
    // Set up any required Proxy credentials 
    if (useDynamicProxy) {
        String user = getProxyUser();
        if (user.length() > 0) {
            httpClient.getState().setProxyCredentials(new AuthScope(proxyHost, proxyPort, null, AuthScope.ANY_SCHEME), new NTCredentials(user, getProxyPass(), localHost, PROXY_DOMAIN));
        } else {
            httpClient.getState().clearProxyCredentials();
        }
    } else {
        if (useStaticProxy) {
            if (PROXY_USER.length() > 0) {
                httpClient.getState().setProxyCredentials(new AuthScope(PROXY_HOST, PROXY_PORT, null, AuthScope.ANY_SCHEME), new NTCredentials(PROXY_USER, PROXY_PASS, localHost, PROXY_DOMAIN));
            }
        } else {
            httpClient.getState().clearProxyCredentials();
        }
    }
    int rto = getResponseTimeout();
    if (rto > 0) {
        httpMethod.getParams().setSoTimeout(rto);
    }
    int cto = getConnectTimeout();
    if (cto > 0) {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(cto);
    }
    // Allow HttpClient to handle the redirects: 
    httpMethod.setFollowRedirects(getAutoRedirects());
    // a well-behaved browser is supposed to send 'Connection: close' 
    // with the last request to an HTTP server. Instead, most browsers 
    // leave it to the server to close the connection after their 
    // timeout period. Leave it to the JMeter user to decide. 
    if (getUseKeepAlive()) {
        httpMethod.setRequestHeader(HTTPConstants.HEADER_CONNECTION, HTTPConstants.KEEP_ALIVE);
    } else {
        httpMethod.setRequestHeader(HTTPConstants.HEADER_CONNECTION, HTTPConstants.CONNECTION_CLOSE);
    }
    setConnectionHeaders(httpMethod, u, getHeaderManager(), getCacheManager());
    String cookies = setConnectionCookie(httpMethod, u, getCookieManager());
    setConnectionAuthorization(httpClient, u, getAuthManager());
    if (res != null) {
        res.setCookies(cookies);
    }
    return httpClient;
}
