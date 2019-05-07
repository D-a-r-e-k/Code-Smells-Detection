/**
     * Reads meta-information from plug-ins archive file and registers found
     * plug-in manifest data with given registry for future analysis.
     * @param archiveFile plug-in archive file
     * @param registry plug-in registry where to register discovered manifests
     *                 for archived plug-ins
     * @return set of UID's of all registered plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     * 
     * @see #readDescriptor(URL, PluginRegistry, PluginArchiver.Filter)
     */
public static Set<String> readDescriptor(final URL archiveFile, final PluginRegistry registry) throws IOException, ClassNotFoundException, ManifestProcessingException {
    return readDescriptor(archiveFile, registry, new Filter() {

        public boolean accept(final String id, final Version version, final boolean isFragment) {
            return true;
        }
    });
}
