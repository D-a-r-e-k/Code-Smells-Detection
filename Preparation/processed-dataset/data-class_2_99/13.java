/**
     * Reads meta-information from plug-ins archive file and registers found
     * plug-in manifest data with given registry for future analysis.
     * <br>
     * <b>Note:</b>
     * <br>
     * In the current implementation all plug-in manifests are extracted to
     * temporary local storage and deleted immediately after their registration
     * with plug-in registry. So manifest URL's are actually point to "fake"
     * locations and main purpose of this method is to allow you to analyze
     * plug-ins archive without needing to download and unpack it.
     * @param archiveFile plug-in archive file
     * @param registry plug-in registry where to register discovered manifests
     *                 for archived plug-ins
     * @param filter filter to be used when un-packing plug-ins
     * @return set of UID's of all registered plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     */
public static Set<String> readDescriptor(final URL archiveFile, final PluginRegistry registry, final Filter filter) throws IOException, ClassNotFoundException, ManifestProcessingException {
    ZipInputStream zipStrm = new ZipInputStream(new BufferedInputStream(archiveFile.openStream()));
    try {
        ZipEntry entry = zipStrm.getNextEntry();
        //NB: we are expecting that descriptor is in the first ZIP entry  
        if (entry == null) {
            throw new IOException("invalid plug-ins archive, no entries found");
        }
        if (!DESCRIPTOR_ENTRY_NAME.equals(entry.getName())) {
            throw new IOException("invalid plug-ins archive " + archiveFile + ", entry " + DESCRIPTOR_ENTRY_NAME + " not found as first ZIP entry in the archive file");
        }
        ObjectInputStream strm = new ObjectInputStream(zipStrm);
        return readDescriptor(strm, registry, Util.getTempFolder(), filter);
    } finally {
        zipStrm.close();
    }
}
