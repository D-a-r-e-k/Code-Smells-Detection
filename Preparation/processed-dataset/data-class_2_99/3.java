private static void pack(final URL url, final String comment, final File destFile) throws IOException {
    ZipOutputStream zipStrm = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile, false)));
    try {
        zipStrm.setComment(comment);
        File file = IoUtil.url2file(url);
        if (file == null) {
            throw new IOException("resolved URL " + url + " is not local file system location pointer");
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            packEntry(zipStrm, null, files[i]);
        }
    } finally {
        zipStrm.close();
    }
}
