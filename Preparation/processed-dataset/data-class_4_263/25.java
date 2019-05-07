/**
	 * Returns the specified text range in a <code>Segment</code>.<p>
	 *
	 * Using a <classname>Segment</classname> is generally more
	 * efficient than using a <classname>String</classname> because it
	 * results in less memory allocation and array copying.<p>
	 *
	 * This method is thread-safe.
	 *
	 * @param start The start offset
	 * @param length The number of characters to get
	 * @param seg The segment to copy the text to
	 */
public void getText(int start, int length, Segment seg) {
    try {
        readLock();
        if (start < 0 || length < 0 || start + length > contentMgr.getLength())
            throw new ArrayIndexOutOfBoundsException(start + ":" + length);
        contentMgr.getText(start, length, seg);
    } finally {
        readUnlock();
    }
}
