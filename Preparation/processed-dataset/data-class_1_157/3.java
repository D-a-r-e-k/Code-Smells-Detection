/**
     * Gets the path segment of the full path to a file (i.e. one
     * which originally included the file name).
     */
private final String extractPath(String file) {
    int lastSepPos = file.lastIndexOf(File.separator);
    return (lastSepPos == -1 ? "" : File.separator + file.substring(0, lastSepPos));
}
