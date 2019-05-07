/**
     * A record is kept of the last-accessed directory for every FileChooser, this method gets that record.
     *
     * @param filechooserKey A unique key.
     * @return File
     */
public static File getFileChooserStartDir(String filechooserKey) {
    File dir = (File) FILECHOOSER_START_DIR.get(filechooserKey);
    return dir == null ? applicationFileDir : dir;
}
