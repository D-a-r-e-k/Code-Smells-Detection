/**
     * Extracts plug-ins from the given archive file.
     * <br>
     * <b>Note:</b>
     * <br>
     * In the current implementation all plug-in manifests are extracted to
     * temporary local storage and deleted immediately after their registration
     * with plug-in registry. So manifest URL's are actually point to "fake"
     * locations.
     * @param archiveFile plug-in archive file
     * @param registry plug-in registry where to register manifests for
     *                 unpacked plug-ins
     * @param destFolder target folder
     * @param filter filter to be used when un-packing plug-ins
     * @return set of UID's of all un-packed (and registered) plug-ins
     * @throws ClassNotFoundException if plug-ins archive descriptor can't be
     *         de-serialized
     * @throws ManifestProcessingException if plug-in manifests can't be
     *         registered
     * @throws IOException if archive damaged or I/O error has occurred
     */
public static Set<String> unpack(final URL archiveFile, final PluginRegistry registry, final File destFolder, final Filter filter) throws IOException, ManifestProcessingException, ClassNotFoundException {
    Set<String> result;
    int count = 0;
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
        result = readDescriptor(strm, registry, destFolder, filter);
        entry = zipStrm.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            if (name.endsWith("/") && (name.lastIndexOf('/', name.length() - 2) == -1)) {
                String uid = name.substring(0, name.length() - 1);
                if (!result.contains(uid)) {
                    entry = zipStrm.getNextEntry();
                    continue;
                }
                count++;
            } else {
                int p = name.indexOf('/');
                if ((p == -1) || (p == 0) || !result.contains(name.substring(0, p))) {
                    entry = zipStrm.getNextEntry();
                    continue;
                }
            }
            unpackEntry(zipStrm, entry, destFolder);
            entry = zipStrm.getNextEntry();
        }
    } finally {
        zipStrm.close();
    }
    if (result.size() != count) {
        throw new IOException("invalid plug-ins number (" + count + ") found in the archive, expected number according to " + "the archive descriptor is " + result.size());
    }
    return result;
}
