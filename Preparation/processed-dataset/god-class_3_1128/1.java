/**
     * Samples the URL passed in and stores the result in
     * <code>HTTPSampleResult</code>, following redirects and downloading
     * page resources as appropriate.
     * <p>
     * When getting a redirect target, redirects are not followed and resources
     * are not downloaded. The caller will take care of this.
     *
     * @param url
     *            URL to sample
     * @param method
     *            HTTP method: GET, POST,...
     * @param areFollowingRedirect
     *            whether we're getting a redirect target
     * @param frameDepth
     *            Depth of this target in the frame structure. Used only to
     *            prevent infinite recursion.
     * @return results of the sampling
     */
@Override
protected HTTPSampleResult sample(URL url, String method, boolean areFollowingRedirect, int frameDepth) {
    String urlStr = url.toString();
    log.debug("Start : sample " + urlStr);
    log.debug("method " + method);
    HttpMethodBase httpMethod = null;
    HTTPSampleResult res = new HTTPSampleResult();
    res.setMonitor(isMonitor());
    res.setSampleLabel(urlStr);
    // May be replaced later 
    res.setHTTPMethod(method);
    res.setURL(url);
    res.sampleStart();
    // Count the retries as well in the time 
    try {
        // May generate IllegalArgumentException 
        if (method.equals(HTTPConstants.POST)) {
            httpMethod = new PostMethod(urlStr);
        } else if (method.equals(HTTPConstants.PUT)) {
            httpMethod = new PutMethod(urlStr);
        } else if (method.equals(HTTPConstants.HEAD)) {
            httpMethod = new HeadMethod(urlStr);
        } else if (method.equals(HTTPConstants.TRACE)) {
            httpMethod = new TraceMethod(urlStr);
        } else if (method.equals(HTTPConstants.OPTIONS)) {
            httpMethod = new OptionsMethod(urlStr);
        } else if (method.equals(HTTPConstants.DELETE)) {
            httpMethod = new DeleteMethod(urlStr);
        } else if (method.equals(HTTPConstants.GET)) {
            httpMethod = new GetMethod(urlStr);
        } else if (method.equals(HTTPConstants.PATCH)) {
            httpMethod = new EntityEnclosingMethod(urlStr) {

                @Override
                public String getName() {
                    // HC3.1 does not have the method 
                    return "PATCH";
                }
            };
        } else {
            throw new IllegalArgumentException("Unexpected method: " + method);
        }
        final CacheManager cacheManager = getCacheManager();
        if (cacheManager != null && HTTPConstants.GET.equalsIgnoreCase(method)) {
            if (cacheManager.inCache(url)) {
                res.sampleEnd();
                res.setResponseNoContent();
                res.setSuccessful(true);
                return res;
            }
        }
        // Set any default request headers 
        setDefaultRequestHeaders(httpMethod);
        // Setup connection 
        HttpClient client = setupConnection(url, httpMethod, res);
        savedClient = client;
        // Handle the various methods 
        if (method.equals(HTTPConstants.POST)) {
            String postBody = sendPostData((PostMethod) httpMethod);
            res.setQueryString(postBody);
        } else if (method.equals(HTTPConstants.PUT) || method.equals(HTTPConstants.PATCH)) {
            String putBody = sendEntityData((EntityEnclosingMethod) httpMethod);
            res.setQueryString(putBody);
        }
        int statusCode = client.executeMethod(httpMethod);
        // Needs to be done after execute to pick up all the headers 
        res.setRequestHeaders(getConnectionHeaders(httpMethod));
        // Request sent. Now get the response: 
        InputStream instream = httpMethod.getResponseBodyAsStream();
        if (instream != null) {
            // will be null for HEAD 
            instream = new CountingInputStream(instream);
            try {
                Header responseHeader = httpMethod.getResponseHeader(HTTPConstants.HEADER_CONTENT_ENCODING);
                if (responseHeader != null && HTTPConstants.ENCODING_GZIP.equals(responseHeader.getValue())) {
                    InputStream tmpInput = new GZIPInputStream(instream);
                    // tmp inputstream needs to have a good counting 
                    res.setResponseData(readResponse(res, tmpInput, (int) httpMethod.getResponseContentLength()));
                } else {
                    res.setResponseData(readResponse(res, instream, (int) httpMethod.getResponseContentLength()));
                }
            } finally {
                JOrphanUtils.closeQuietly(instream);
            }
        }
        res.sampleEnd();
        // Done with the sampling proper. 
        // Now collect the results into the HTTPSampleResult: 
        res.setSampleLabel(httpMethod.getURI().toString());
        // Pick up Actual path (after redirects) 
        res.setResponseCode(Integer.toString(statusCode));
        res.setSuccessful(isSuccessCode(statusCode));
        res.setResponseMessage(httpMethod.getStatusText());
        String ct = null;
        Header h = httpMethod.getResponseHeader(HTTPConstants.HEADER_CONTENT_TYPE);
        if (h != null) // Can be missing, e.g. on redirect 
        {
            ct = h.getValue();
            res.setContentType(ct);
            // e.g. text/html; charset=ISO-8859-1 
            res.setEncodingAndType(ct);
        }
        res.setResponseHeaders(getResponseHeaders(httpMethod));
        if (res.isRedirect()) {
            final Header headerLocation = httpMethod.getResponseHeader(HTTPConstants.HEADER_LOCATION);
            if (headerLocation == null) {
                // HTTP protocol violation, but avoids NPE 
                throw new IllegalArgumentException("Missing location header");
            }
            res.setRedirectLocation(headerLocation.getValue());
        }
        // record some sizes to allow HTTPSampleResult.getBytes() with different options 
        if (instream != null) {
            res.setBodySize(((CountingInputStream) instream).getCount());
        }
        res.setHeadersSize(calculateHeadersSize(httpMethod));
        if (log.isDebugEnabled()) {
            log.debug("Response headersSize=" + res.getHeadersSize() + " bodySize=" + res.getBodySize() + " Total=" + (res.getHeadersSize() + res.getBodySize()));
        }
        // If we redirected automatically, the URL may have changed 
        if (getAutoRedirects()) {
            res.setURL(new URL(httpMethod.getURI().toString()));
        }
        // Store any cookies received in the cookie manager: 
        saveConnectionCookies(httpMethod, res.getURL(), getCookieManager());
        // Save cache information 
        if (cacheManager != null) {
            cacheManager.saveDetails(httpMethod, res);
        }
        // Follow redirects and download page resources if appropriate: 
        res = resultProcessing(areFollowingRedirect, frameDepth, res);
        log.debug("End : sample");
        return res;
    } catch (IllegalArgumentException e) {
        // e.g. some kinds of invalid URL 
        res.sampleEnd();
        // pick up headers if failed to execute the request 
        // httpMethod can be null if method is unexpected 
        if (httpMethod != null) {
            res.setRequestHeaders(getConnectionHeaders(httpMethod));
        }
        errorResult(e, res);
        return res;
    } catch (IOException e) {
        res.sampleEnd();
        // pick up headers if failed to execute the request 
        res.setRequestHeaders(getConnectionHeaders(httpMethod));
        errorResult(e, res);
        return res;
    } finally {
        savedClient = null;
        if (httpMethod != null) {
            httpMethod.releaseConnection();
        }
    }
}
