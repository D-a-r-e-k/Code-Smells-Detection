/**
     * Determine if we should use multipart/form-data or
     * application/x-www-form-urlencoded for the post
     *
     * @return true if multipart/form-data should be used and method is POST
     */
public boolean getUseMultipartForPost() {
    // We use multipart if we have been told so, or files are present 
    // and the files should not be send as the post body 
    HTTPFileArg[] files = getHTTPFiles();
    if (HTTPConstants.POST.equals(getMethod()) && (getDoMultipartPost() || (files.length > 0 && !getSendFileAsPostBody()))) {
        return true;
    }
    return false;
}
