/**
   * Advanced: set how many times to try incrementing the
   * gen when loading the segments file.  This only runs if
   * the primary (listing directory) and secondary (opening
   * segments.gen file) methods fail to find the segments
   * file.
   *
   * @lucene.experimental
   */
public static void setDefaultGenLookaheadCount(int count) {
    defaultGenLookaheadCount = count;
}
