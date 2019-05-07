private static void unpackEntry(final ZipInputStream zipStrm, final ZipEntry entry, final File destFolder) throws IOException {
    String name = entry.getName();
    if (name.endsWith("/")) {
        //$NON-NLS-1$  
        File folder = new File(destFolder.getCanonicalPath() + "/" + name);
        //$NON-NLS-1$  
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("can't create folder " + folder);
        }
        folder.setLastModified(entry.getTime());
        return;
    }
    File file = new File(destFolder.getCanonicalPath() + "/" + name);
    //$NON-NLS-1$  
    File folder = file.getParentFile();
    if (!folder.exists() && !folder.mkdirs()) {
        throw new IOException("can't create folder " + folder);
    }
    OutputStream strm = new BufferedOutputStream(new FileOutputStream(file, false));
    try {
        IoUtil.copyStream(zipStrm, strm, 1024);
    } finally {
        strm.close();
    }
    file.setLastModified(entry.getTime());
}
