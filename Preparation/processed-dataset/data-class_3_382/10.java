/** Find the latest commit ({@code segments_N file}) and
   *  load all {@link SegmentInfoPerCommit}s. */
public final void read(Directory directory) throws IOException {
    generation = lastGeneration = -1;
    new FindSegmentsFile(directory) {

        @Override
        protected Object doBody(String segmentFileName) throws IOException {
            read(directory, segmentFileName);
            return null;
        }
    }.run();
}
