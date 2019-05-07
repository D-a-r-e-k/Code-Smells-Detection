/**
   * Parse the generation off the segments file name and
   * return it.
   */
public static long generationFromSegmentsFileName(String fileName) {
    if (fileName.equals(IndexFileNames.SEGMENTS)) {
        return 0;
    } else if (fileName.startsWith(IndexFileNames.SEGMENTS)) {
        return Long.parseLong(fileName.substring(1 + IndexFileNames.SEGMENTS.length()), Character.MAX_RADIX);
    } else {
        throw new IllegalArgumentException("fileName \"" + fileName + "\" is not a segments file");
    }
}
