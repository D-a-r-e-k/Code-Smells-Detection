/**
     * Determine if the file should be sent as the entire Content body,
     * i.e. without any additional wrapping.
     *
     * @return true if specified file is to be sent as the body,
     * i.e. there is a single file entry which has a non-empty path and
     * an empty Parameter name.
     */
public boolean getSendFileAsPostBody() {
    // If there is one file with no parameter name, the file will 
    // be sent as post body. 
    HTTPFileArg[] files = getHTTPFiles();
    return (files.length == 1) && (files[0].getPath().length() > 0) && (files[0].getParamName().length() == 0);
}
