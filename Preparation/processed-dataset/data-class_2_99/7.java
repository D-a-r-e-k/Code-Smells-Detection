/**
     * Extracts plug-ins from the given archive file.
     * @param archiveFile plug-in archive file
     * @param registry plug-in registry where to register manifests for
     *                 unpacked plug-ins
     * @param destFolder target folder
     * @return set of UID's of all un-packed (and registered) plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     * 
     * @see #unpack(URL, PluginRegistry, File, PluginArchiver.Filter)
     */
public static Set<String> unpack(final URL archiveFile, final PluginRegistry registry, final File destFolder) throws ManifestProcessingException, IOException, ClassNotFoundException {
    return unpack(archiveFile, registry, destFolder, new Filter() {

        public boolean accept(final String id, final Version version, final boolean isFragment) {
            return true;
        }
    });
}
