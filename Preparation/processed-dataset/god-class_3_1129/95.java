/**
     * Follow redirects and download page resources if appropriate. this works,
     * but the container stuff here is what's doing it. followRedirects() is
     * actually doing the work to make sure we have only one container to make
     * this work more naturally, I think this method - sample() - needs to take
     * an HTTPSamplerResult container parameter instead of a
     * boolean:areFollowingRedirect.
     *
     * @param areFollowingRedirect
     * @param frameDepth
     * @param res
     * @return the sample result
     */
protected HTTPSampleResult resultProcessing(boolean areFollowingRedirect, int frameDepth, HTTPSampleResult res) {
    boolean wasRedirected = false;
    if (!areFollowingRedirect) {
        if (res.isRedirect()) {
            log.debug("Location set to - " + res.getRedirectLocation());
            if (getFollowRedirects()) {
                res = followRedirects(res, frameDepth);
                areFollowingRedirect = true;
                wasRedirected = true;
            }
        }
    }
    if (isImageParser() && (SampleResult.TEXT).equals(res.getDataType()) && res.isSuccessful()) {
        if (frameDepth > MAX_FRAME_DEPTH) {
            res.addSubResult(errorResult(new Exception("Maximum frame/iframe nesting depth exceeded."), new HTTPSampleResult(res)));
        } else {
            // Only download page resources if we were not redirected. 
            // If we were redirected, the page resources have already been 
            // downloaded for the sample made for the redirected url 
            // otherwise, use null so the container is created if necessary unless 
            // the flag is false, in which case revert to broken 2.1 behaviour  
            // Bug 51939 -  https://issues.apache.org/bugzilla/show_bug.cgi?id=51939 
            if (!wasRedirected) {
                HTTPSampleResult container = (HTTPSampleResult) (areFollowingRedirect ? res.getParent() : SEPARATE_CONTAINER ? null : res);
                res = downloadPageResources(res, container, frameDepth);
            }
        }
    }
    return res;
}
