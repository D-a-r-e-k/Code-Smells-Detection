/*
     * Method to set files list to be uploaded.
     *
     * @param value
     *   HTTPFileArgs object that stores file list to be uploaded.
     */
private void setHTTPFileArgs(HTTPFileArgs value) {
    if (value.getHTTPFileArgCount() > 0) {
        setProperty(new TestElementProperty(FILE_ARGS, value));
    } else {
        removeProperty(FILE_ARGS);
    }
}
