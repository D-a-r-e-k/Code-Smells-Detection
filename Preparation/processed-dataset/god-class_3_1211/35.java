/** applies all changes caused by committing a merge to this SegmentInfos */
void applyMergeChanges(MergePolicy.OneMerge merge, boolean dropSegment) {
    final Set<SegmentInfoPerCommit> mergedAway = new HashSet<SegmentInfoPerCommit>(merge.segments);
    boolean inserted = false;
    int newSegIdx = 0;
    for (int segIdx = 0, cnt = segments.size(); segIdx < cnt; segIdx++) {
        assert segIdx >= newSegIdx;
        final SegmentInfoPerCommit info = segments.get(segIdx);
        if (mergedAway.contains(info)) {
            if (!inserted && !dropSegment) {
                segments.set(segIdx, merge.info);
                inserted = true;
                newSegIdx++;
            }
        } else {
            segments.set(newSegIdx, info);
            newSegIdx++;
        }
    }
    // the rest of the segments in list are duplicates, so don't remove from map, only list! 
    segments.subList(newSegIdx, segments.size()).clear();
    // Either we found place to insert segment, or, we did 
    // not, but only because all segments we merged becamee 
    // deleted while we are merging, in which case it should 
    // be the case that the new segment is also all deleted, 
    // we insert it at the beginning if it should not be dropped: 
    if (!inserted && !dropSegment) {
        segments.add(0, merge.info);
    }
}
