/** Returns sum of all segment's docCounts.  Note that
   *  this does not include deletions */
public int totalDocCount() {
    int count = 0;
    for (SegmentInfoPerCommit info : this) {
        count += info.info.getDocCount();
    }
    return count;
}
