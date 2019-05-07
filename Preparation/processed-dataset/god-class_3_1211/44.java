/** Remove the provided {@link SegmentInfoPerCommit}.
   *
   * <p><b>WARNING</b>: O(N) cost */
public void remove(SegmentInfoPerCommit si) {
    segments.remove(si);
}
