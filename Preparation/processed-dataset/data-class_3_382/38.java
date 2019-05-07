/** Returns an <b>unmodifiable</b> {@link Iterator} of contained segments in order. */
// @Override (comment out until Java 6) 
@Override
public Iterator<SegmentInfoPerCommit> iterator() {
    return asList().iterator();
}
