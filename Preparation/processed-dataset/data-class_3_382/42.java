/** Appends the provided {@link SegmentInfoPerCommit}s. */
public void addAll(Iterable<SegmentInfoPerCommit> sis) {
    for (final SegmentInfoPerCommit si : sis) {
        this.add(si);
    }
}
