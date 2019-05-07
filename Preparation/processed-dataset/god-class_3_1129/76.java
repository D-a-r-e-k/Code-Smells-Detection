/**
     * Gets the QueryString attribute of the UrlConfig object, using the
     * specified encoding to encode the parameter values put into the URL
     *
     * @param contentEncoding the encoding to use for encoding parameter values
     * @return the QueryString value
     */
public String getQueryString(String contentEncoding) {
    // Check if the sampler has a specified content encoding 
    if (JOrphanUtils.isBlank(contentEncoding)) {
        // We use the encoding which should be used according to the HTTP spec, which is UTF-8 
        contentEncoding = EncoderCache.URL_ARGUMENT_ENCODING;
    }
    StringBuilder buf = new StringBuilder();
    PropertyIterator iter = getArguments().iterator();
    boolean first = true;
    while (iter.hasNext()) {
        HTTPArgument item = null;
        /*
             * N.B. Revision 323346 introduced the ClassCast check, but then used iter.next()
             * to fetch the item to be cast, thus skipping the element that did not cast.
             * Reverted to work more like the original code, but with the check in place.
             * Added a warning message so can track whether it is necessary
             */
        Object objectValue = iter.next().getObjectValue();
        try {
            item = (HTTPArgument) objectValue;
        } catch (ClassCastException e) {
            log.warn("Unexpected argument type: " + objectValue.getClass().getName());
            item = new HTTPArgument((Argument) objectValue);
        }
        final String encodedName = item.getEncodedName();
        if (encodedName.length() == 0) {
            continue;
        }
        if (!first) {
            buf.append(QRY_SEP);
        } else {
            first = false;
        }
        buf.append(encodedName);
        if (item.getMetaData() == null) {
            buf.append(ARG_VAL_SEP);
        } else {
            buf.append(item.getMetaData());
        }
        // Encode the parameter value in the specified content encoding 
        try {
            buf.append(item.getEncodedValue(contentEncoding));
        } catch (UnsupportedEncodingException e) {
            log.warn("Unable to encode parameter in encoding " + contentEncoding + ", parameter value not included in query string");
        }
    }
    return buf.toString();
}
