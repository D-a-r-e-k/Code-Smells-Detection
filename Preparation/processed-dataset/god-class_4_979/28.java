/**
     * Returns an <code>InputStream</code> from the fileName as a resource.
     * 
     * @param fileName
     *          file name.
     * @return an <code>InputStream</code> from the fileName as a resource.
     */
protected InputStream getInputStream(String fileName) {
    return this.classLoadHelper.getResourceAsStream(fileName);
}
