/**
     * @see org.java.plugin.registry.PluginRegistry#readManifestInfo(
     *      java.net.URL)
     */
public ManifestInfo readManifestInfo(final URL url) throws ManifestProcessingException {
    try {
        return new ManifestInfoImpl(manifestParser.parseManifestInfo(url));
    } catch (Exception e) {
        throw new ManifestProcessingException(PACKAGE_NAME, "manifestParsingError", url, e);
    }
}
