/**
     * Returns an <code>URL</code> from the fileName as a resource.
     * 
     * @param fileName
     *          file name.
     * @return an <code>URL</code> from the fileName as a resource.
     */
protected URL getURL(String fileName) {
    return classLoadHelper.getResource(fileName);
}
