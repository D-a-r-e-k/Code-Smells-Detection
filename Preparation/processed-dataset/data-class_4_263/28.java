/**
	 * Inserts a string into the buffer.
	 * @param offset The offset
	 * @param seg The segment
	 * @since jEdit 4.0pre1
	 */
public void insert(int offset, Segment seg) {
    if (seg.count == 0)
        return;
    if (isReadOnly())
        throw new RuntimeException("buffer read-only");
    try {
        writeLock();
        if (offset < 0 || offset > contentMgr.getLength())
            throw new ArrayIndexOutOfBoundsException(offset);
        contentMgr.insert(offset, seg);
        integerArray.clear();
        for (int i = 0; i < seg.count; i++) {
            if (seg.array[seg.offset + i] == '\n')
                integerArray.add(i + 1);
        }
        if (!undoInProgress) {
            undoMgr.contentInserted(offset, seg.count, seg.toString(), !dirty);
        }
        contentInserted(offset, seg.count, integerArray);
    } finally {
        writeUnlock();
    }
}
