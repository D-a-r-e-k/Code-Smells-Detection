/**
     * Iteratively download the redirect targets of a redirect response.
     * <p>
     * The returned result will contain one subsample for each request issued,
     * including the original one that was passed in. It will be an
     * HTTPSampleResult that should mostly look as if the final destination of
     * the redirect chain had been obtained in a single shot.
     *
     * @param res
     *            result of the initial request - must be a redirect response
     * @param frameDepth
     *            Depth of this target in the frame structure. Used only to
     *            prevent infinite recursion.
     * @return "Container" result with one subsample per request issued
     */
protected HTTPSampleResult followRedirects(HTTPSampleResult res, int frameDepth) {
    HTTPSampleResult totalRes = new HTTPSampleResult(res);
    totalRes.addRawSubResult(res);
    HTTPSampleResult lastRes = res;
    int redirect;
    for (redirect = 0; redirect < MAX_REDIRECTS; redirect++) {
        boolean invalidRedirectUrl = false;
        String location = lastRes.getRedirectLocation();
        if (REMOVESLASHDOTDOT) {
            location = ConversionUtils.removeSlashDotDot(location);
        }
        // Browsers seem to tolerate Location headers with spaces, 
        // replacing them automatically with %20. We want to emulate 
        // this behaviour. 
        location = encodeSpaces(location);
        try {
            lastRes = sample(ConversionUtils.makeRelativeURL(lastRes.getURL(), location), HTTPConstants.GET, true, frameDepth);
        } catch (MalformedURLException e) {
            errorResult(e, lastRes);
            // The redirect URL we got was not a valid URL 
            invalidRedirectUrl = true;
        }
        if (lastRes.getSubResults() != null && lastRes.getSubResults().length > 0) {
            SampleResult[] subs = lastRes.getSubResults();
            for (int i = 0; i < subs.length; i++) {
                totalRes.addSubResult(subs[i]);
            }
        } else {
            // Only add sample if it is a sample of valid url redirect, i.e. that 
            // we have actually sampled the URL 
            if (!invalidRedirectUrl) {
                totalRes.addSubResult(lastRes);
            }
        }
        if (!lastRes.isRedirect()) {
            break;
        }
    }
    if (redirect >= MAX_REDIRECTS) {
        lastRes = errorResult(new IOException("Exceeeded maximum number of redirects: " + MAX_REDIRECTS), new HTTPSampleResult(lastRes));
        totalRes.addSubResult(lastRes);
    }
    // Now populate the any totalRes fields that need to 
    // come from lastRes: 
    totalRes.setSampleLabel(totalRes.getSampleLabel() + "->" + lastRes.getSampleLabel());
    // The following three can be discussed: should they be from the 
    // first request or from the final one? I chose to do it this way 
    // because that's what browsers do: they show the final URL of the 
    // redirect chain in the location field. 
    totalRes.setURL(lastRes.getURL());
    totalRes.setHTTPMethod(lastRes.getHTTPMethod());
    totalRes.setQueryString(lastRes.getQueryString());
    totalRes.setRequestHeaders(lastRes.getRequestHeaders());
    totalRes.setResponseData(lastRes.getResponseData());
    totalRes.setResponseCode(lastRes.getResponseCode());
    totalRes.setSuccessful(lastRes.isSuccessful());
    totalRes.setResponseMessage(lastRes.getResponseMessage());
    totalRes.setDataType(lastRes.getDataType());
    totalRes.setResponseHeaders(lastRes.getResponseHeaders());
    totalRes.setContentType(lastRes.getContentType());
    totalRes.setDataEncoding(lastRes.getDataEncodingNoDefault());
    return totalRes;
}
