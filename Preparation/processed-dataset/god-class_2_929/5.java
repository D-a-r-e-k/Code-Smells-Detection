/**
     * Packs plug-ins from given registry as one archive file according to
     * given filter.
     * @param registry plug-ins registry
     * @param pathResolver path resolver (only local file URLs are supported)
     * @param destFile target archive file (will be overridden if any exists)
     * @param filter filter to be used when packing plug-ins
     * @return set of UID's of all packed plug-ins
     * @throws IOException if an I/O error has occurred
     */
public static Set<String> pack(final PluginRegistry registry, final PathResolver pathResolver, final File destFile, final Filter filter) throws IOException {
    Set<String> result;
    ZipOutputStream zipStrm = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile, false)));
    try {
        zipStrm.setComment("JPF plug-ins archive");
        //$NON-NLS-1$  
        ZipEntry entry = new ZipEntry(DESCRIPTOR_ENTRY_NAME);
        entry.setComment("JPF plug-ins archive descriptor");
        //$NON-NLS-1$  
        zipStrm.putNextEntry(entry);
        result = writeDescripor(registry, filter, new ObjectOutputStream(zipStrm));
        zipStrm.closeEntry();
        for (PluginDescriptor descr : registry.getPluginDescriptors()) {
            if (!result.contains(descr.getUniqueId())) {
                continue;
            }
            URL url = pathResolver.resolvePath(descr, "/");
            //$NON-NLS-1$  
            File file = IoUtil.url2file(url);
            if (file == null) {
                throw new IOException("resolved URL " + url + " is not local file system location pointer");
            }
            entry = new ZipEntry(descr.getUniqueId() + "/");
            //$NON-NLS-1$  
            entry.setComment("Content for JPF plug-in " + descr.getId() + " version " + descr.getVersion());
            //$NON-NLS-1$  
            entry.setTime(file.lastModified());
            zipStrm.putNextEntry(entry);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                packEntry(zipStrm, entry, files[i]);
            }
        }
        for (PluginFragment fragment : registry.getPluginFragments()) {
            if (!result.contains(fragment.getUniqueId())) {
                continue;
            }
            URL url = pathResolver.resolvePath(fragment, "/");
            //$NON-NLS-1$  
            File file = IoUtil.url2file(url);
            if (file == null) {
                throw new IOException("resolved URL " + url + " is not local file system location pointer");
            }
            entry = new ZipEntry(fragment.getUniqueId() + "/");
            //$NON-NLS-1$  
            entry.setComment("Content for JPF plug-in fragment " + fragment.getId() + " version " + fragment.getVersion());
            entry.setTime(file.lastModified());
            zipStrm.putNextEntry(entry);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                packEntry(zipStrm, entry, files[i]);
            }
        }
    } finally {
        zipStrm.close();
    }
    return result;
}
