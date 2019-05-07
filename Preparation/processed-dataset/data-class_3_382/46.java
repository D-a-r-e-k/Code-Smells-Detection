/** Return true if the provided {@link
   *  SegmentInfoPerCommit} is contained.
   *
   * <p><b>WARNING</b>: O(N) cost */
boolean contains(SegmentInfoPerCommit si) {
    return segments.contains(si);
}
