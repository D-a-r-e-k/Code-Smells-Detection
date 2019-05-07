/**
     * A record is kept of the last-accessed directory for every FileChooser, this method sets that record.
     *
     * @param filechooserKey A unique key.
     * @param dir            The new directory.
     */
public static void setFileChooserStartDir(String filechooserKey, File dir) {
    FILECHOOSER_START_DIR.put(filechooserKey, dir);
}
