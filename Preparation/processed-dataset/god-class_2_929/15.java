private static Set<String> readDescriptor(final ObjectInputStream strm, final PluginRegistry registry, final File tempFolder, final Filter filter) throws IOException, ClassNotFoundException, ManifestProcessingException {
    ArchiveDescriptorEntry[] data = (ArchiveDescriptorEntry[]) strm.readObject();
    // For simplicity we'll store manifests to a temporary files rather than  
    // create special URL's and provide special URL handler for them.  
    // More powerful approach will be possibly implemented in the future.  
    Set<URL> urls = new HashSet<URL>();
    Set<File> files = new HashSet<File>();
    for (int i = 0; i < data.length; i++) {
        if (!filter.accept(data[i].getId(), data[i].getVersion(), data[i].isFragment())) {
            continue;
        }
        File file = File.createTempFile("manifest.", null, tempFolder);
        //$NON-NLS-1$  
        file.deleteOnExit();
        OutputStream fileStrm = new BufferedOutputStream(new FileOutputStream(file, false));
        try {
            fileStrm.write(data[i].getData());
        } finally {
            fileStrm.close();
        }
        files.add(file);
        urls.add(IoUtil.file2url(file));
    }
    Set<String> result = new HashSet<String>();
    try {
        for (Identity obj : registry.register(urls.toArray(new URL[urls.size()])).values()) {
            if (obj instanceof PluginDescriptor) {
                result.add(((PluginDescriptor) obj).getUniqueId());
            } else if (obj instanceof PluginFragment) {
                result.add(((PluginFragment) obj).getUniqueId());
            } else {
            }
        }
    } finally {
        for (File file : files) {
            file.delete();
        }
    }
    return result;
}
