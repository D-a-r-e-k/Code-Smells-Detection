/**
     * Process the xml file named <code>fileName</code>.
     * 
     * @param fileName
     *          meta data file name.
     */
protected void processFile(String fileName) throws Exception {
    processFile(fileName, getSystemIdForFileName(fileName));
}
