/**
     * Sets the Path attribute of the UrlConfig object Also calls parseArguments
     * to extract and store any query arguments
     *
     * @param path
     *            The new Path value
     */
public void setPath(String path) {
    // We know that URL arguments should always be encoded in UTF-8 according to spec 
    setPath(path, EncoderCache.URL_ARGUMENT_ENCODING);
}
