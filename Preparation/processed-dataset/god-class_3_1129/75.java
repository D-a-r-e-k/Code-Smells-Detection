/**
     * Gets the QueryString attribute of the UrlConfig object, using
     * UTF-8 to encode the URL
     *
     * @return the QueryString value
     */
public String getQueryString() {
    // We use the encoding which should be used according to the HTTP spec, which is UTF-8 
    return getQueryString(EncoderCache.URL_ARGUMENT_ENCODING);
}
