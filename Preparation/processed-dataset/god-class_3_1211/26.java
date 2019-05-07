/** Returns all file names referenced by SegmentInfo
   *  instances matching the provided Directory (ie files
   *  associated with any "external" segments are skipped).
   *  The returned collection is recomputed on each
   *  invocation.  */
public Collection<String> files(Directory dir, boolean includeSegmentsFile) throws IOException {
    HashSet<String> files = new HashSet<String>();
    if (includeSegmentsFile) {
        final String segmentFileName = getSegmentsFileName();
        if (segmentFileName != null) {
            /*
         * TODO: if lastGen == -1 we get might get null here it seems wrong to
         * add null to the files set
         */
            files.add(segmentFileName);
        }
    }
    final int size = size();
    for (int i = 0; i < size; i++) {
        final SegmentInfoPerCommit info = info(i);
        assert info.info.dir == dir;
        if (info.info.dir == dir) {
            files.addAll(info.files());
        }
    }
    return files;
}
