/**
   * Creates the necessary directories.
   */
public static void initDirectories() {
    File dir = new File(SETTINGS_DIRECTORY);
    if (!dir.exists()) {
        dir.mkdir();
        dir = new File(SETTINGS_DIRECTORY + "plugins" + File.separator);
        if (!dir.exists())
            dir.mkdir();
        dir = new File(SETTINGS_DIRECTORY + "scripts" + File.separator);
        if (!dir.exists())
            dir.mkdir();
        dir = new File(SETTINGS_DIRECTORY + "xinsert" + File.separator);
        if (!dir.exists())
            dir.mkdir();
    }
}
