/**
     * Extracts all plug-ins from the given archive file.
     * <br>
     * <b>Note:</b>
     * <br>
     * {@link ObjectFactory#createRegistry() Standard plug-in registry}
     * implementation will be used internally to read plug-in manifests.
     * @param archiveFile plug-in archive file
     * @param destFolder target folder
     * @return set of UID's of all un-packed plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     * 
     * @see ObjectFactory#createRegistry()
     */
public static Set<String> unpack(final URL archiveFile, final File destFolder) throws ManifestProcessingException, IOException, ClassNotFoundException {
    return unpack(archiveFile, ObjectFactory.newInstance().createRegistry(), destFolder);
}
