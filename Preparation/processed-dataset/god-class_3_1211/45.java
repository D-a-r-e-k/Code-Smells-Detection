/** Remove the {@link SegmentInfoPerCommit} at the
   * provided index.
   *
   * <p><b>WARNING</b>: O(N) cost */
void remove(int index) {
    segments.remove(index);
}
