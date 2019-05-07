/** Returns all contained segments as an <b>unmodifiable</b> {@link List} view. */
public List<SegmentInfoPerCommit> asList() {
    return Collections.unmodifiableList(segments);
}
