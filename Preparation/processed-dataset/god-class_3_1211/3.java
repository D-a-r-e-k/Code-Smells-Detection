/**
   * Get the generation of the most recent commit to the
   * index in this directory (N in the segments_N file).
   *
   * @param directory -- directory to search for the latest segments_N file
   */
public static long getLastCommitGeneration(Directory directory) throws IOException {
    try {
        return getLastCommitGeneration(directory.listAll());
    } catch (NoSuchDirectoryException nsde) {
        return -1;
    }
}
