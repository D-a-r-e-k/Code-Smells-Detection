/**
   * Get the filename of the segments_N file for the most
   * recent commit in the list of index files.
   *
   * @param files -- array of file names to check
   */
public static String getLastCommitSegmentsFileName(String[] files) {
    return IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS, "", getLastCommitGeneration(files));
}
