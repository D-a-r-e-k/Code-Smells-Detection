/**
   * Get the generation of the most recent commit to the
   * list of index files (N in the segments_N file).
   *
   * @param files -- array of file names to check
   */
public static long getLastCommitGeneration(String[] files) {
    if (files == null) {
        return -1;
    }
    long max = -1;
    for (String file : files) {
        if (file.startsWith(IndexFileNames.SEGMENTS) && !file.equals(IndexFileNames.SEGMENTS_GEN)) {
            long gen = generationFromSegmentsFileName(file);
            if (gen > max) {
                max = gen;
            }
        }
    }
    return max;
}
