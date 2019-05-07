/**
     * Creates an HTTPArgument and adds it to the current set {@link #getArguments()} of arguments.
     * 
     * @param name - the parameter name
     * @param value - the parameter value
     * @param metaData - normally just '='
     * @param contentEncoding - the encoding, may be null
     */
public void addEncodedArgument(String name, String value, String metaData, String contentEncoding) {
    if (log.isDebugEnabled()) {
        log.debug("adding argument: name: " + name + " value: " + value + " metaData: " + metaData + " contentEncoding: " + contentEncoding);
    }
    HTTPArgument arg = null;
    final boolean nonEmptyEncoding = !StringUtils.isEmpty(contentEncoding);
    if (nonEmptyEncoding) {
        arg = new HTTPArgument(name, value, metaData, true, contentEncoding);
    } else {
        arg = new HTTPArgument(name, value, metaData, true);
    }
    // Check if there are any difference between name and value and their encoded name and value 
    String valueEncoded = null;
    if (nonEmptyEncoding) {
        try {
            valueEncoded = arg.getEncodedValue(contentEncoding);
        } catch (UnsupportedEncodingException e) {
            log.warn("Unable to get encoded value using encoding " + contentEncoding);
            valueEncoded = arg.getEncodedValue();
        }
    } else {
        valueEncoded = arg.getEncodedValue();
    }
    // If there is no difference, we mark it as not needing encoding 
    if (arg.getName().equals(arg.getEncodedName()) && arg.getValue().equals(valueEncoded)) {
        arg.setAlwaysEncoded(false);
    }
    this.getArguments().addArgument(arg);
}
