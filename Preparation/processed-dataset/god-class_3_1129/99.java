/*
     * Method to get files list to be uploaded.
     */
private HTTPFileArgs getHTTPFileArgs() {
    return (HTTPFileArgs) getProperty(FILE_ARGS).getObjectValue();
}
