private static void packEntry(final ZipOutputStream zipStrm, final ZipEntry parentEntry, final File file) throws IOException {
    String parentEntryName = (parentEntry == null) ? "" : parentEntry.getName();
    if (file.isFile()) {
        ZipEntry entry = new ZipEntry(parentEntryName + file.getName());
        entry.setTime(file.lastModified());
        zipStrm.putNextEntry(entry);
        BufferedInputStream fileStrm = new BufferedInputStream(new FileInputStream(file));
        try {
            IoUtil.copyStream(fileStrm, zipStrm, 1024);
        } finally {
            fileStrm.close();
        }
        return;
    }
    ZipEntry entry = new ZipEntry(parentEntryName + file.getName() + "/");
    //$NON-NLS-1$  
    entry.setTime(file.lastModified());
    zipStrm.putNextEntry(entry);
    File[] files = file.listFiles();
    for (int i = 0; i < files.length; i++) {
        packEntry(zipStrm, entry, files[i]);
    }
}
