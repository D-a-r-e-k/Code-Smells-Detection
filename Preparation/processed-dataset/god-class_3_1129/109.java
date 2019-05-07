/**
     * JMeter 2.3.1 and earlier only had fields for one file on the GUI:
     * - FILE_NAME
     * - FILE_FIELD
     * - MIMETYPE
     * These were stored in their own individual properties.
     *
     * Version 2.3.3 introduced a list of files, each with their own path, name and mimetype.
     *
     * In order to maintain backwards compatibility of test plans, the 3 original properties
     * were retained; additional file entries are stored in an HTTPFileArgs class.
     * The HTTPFileArgs class was only present if there is more than 1 file; this means that
     * such test plans are backward compatible.
     *
     * Versions after 2.3.4 dispense with the original set of 3 properties.
     * Test plans that use them are converted to use a single HTTPFileArgs list.
     *
     * @see HTTPSamplerBaseConverter
     */
void mergeFileProperties() {
    JMeterProperty fileName = getProperty(FILE_NAME);
    JMeterProperty paramName = getProperty(FILE_FIELD);
    JMeterProperty mimeType = getProperty(MIMETYPE);
    HTTPFileArg oldStyleFile = new HTTPFileArg(fileName, paramName, mimeType);
    HTTPFileArgs fileArgs = getHTTPFileArgs();
    HTTPFileArgs allFileArgs = new HTTPFileArgs();
    if (oldStyleFile.isNotEmpty()) {
        // OK, we have an old-style file definition 
        allFileArgs.addHTTPFileArg(oldStyleFile);
        // save it 
        // Now deal with any additional file arguments 
        if (fileArgs != null) {
            HTTPFileArg[] infiles = fileArgs.asArray();
            for (int i = 0; i < infiles.length; i++) {
                allFileArgs.addHTTPFileArg(infiles[i]);
            }
        }
    } else {
        if (fileArgs != null) {
            // for new test plans that don't have FILE/PARAM/MIME properties 
            allFileArgs = fileArgs;
        }
    }
    // Updated the property lists 
    setHTTPFileArgs(allFileArgs);
    removeProperty(FILE_FIELD);
    removeProperty(FILE_NAME);
    removeProperty(MIMETYPE);
}
