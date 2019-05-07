// Mark Walsh 2002-08-03, modified to also parse a parameter name value 
// string, where string contains only the parameter name and no equal sign. 
/**
     * This method allows a proxy server to send over the raw text from a
     * browser's output stream to be parsed and stored correctly into the
     * UrlConfig object.
     *
     * For each name found, addArgument() is called
     *
     * @param queryString -
     *            the query string, might be the post body of a http post request.
     * @param contentEncoding -
     *            the content encoding of the query string; 
     *            if non-null then it is used to decode the 
     */
public void parseArguments(String queryString, String contentEncoding) {
    String[] args = JOrphanUtils.split(queryString, QRY_SEP);
    for (int i = 0; i < args.length; i++) {
        // need to handle four cases: 
        // - string contains name=value 
        // - string contains name= 
        // - string contains name 
        // - empty string 
        String metaData;
        // records the existance of an equal sign 
        String name;
        String value;
        int length = args[i].length();
        int endOfNameIndex = args[i].indexOf(ARG_VAL_SEP);
        if (endOfNameIndex != -1) {
            // is there a separator? 
            // case of name=value, name= 
            metaData = ARG_VAL_SEP;
            name = args[i].substring(0, endOfNameIndex);
            value = args[i].substring(endOfNameIndex + 1, length);
        } else {
            metaData = "";
            name = args[i];
            value = "";
        }
        if (name.length() > 0) {
            // If we know the encoding, we can decode the argument value, 
            // to make it easier to read for the user 
            if (!StringUtils.isEmpty(contentEncoding)) {
                addEncodedArgument(name, value, metaData, contentEncoding);
            } else {
                // If we do not know the encoding, we just use the encoded value 
                // The browser has already done the encoding, so save the values as is 
                addNonEncodedArgument(name, value, metaData);
            }
        }
    }
}
