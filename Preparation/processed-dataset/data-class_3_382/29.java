/** Returns readable description of this segment. */
public String toString(Directory directory) {
    StringBuilder buffer = new StringBuilder();
    buffer.append(getSegmentsFileName()).append(": ");
    final int count = size();
    for (int i = 0; i < count; i++) {
        if (i > 0) {
            buffer.append(' ');
        }
        final SegmentInfoPerCommit info = info(i);
        buffer.append(info.toString(directory, 0));
    }
    return buffer.toString();
}
