/**
   * Get the segments_N filename in use by this segment infos.
   */
public String getSegmentsFileName() {
    return IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS, "", lastGeneration);
}
