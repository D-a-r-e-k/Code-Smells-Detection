/**
	 * Returns the specified line in a <code>Segment</code>.<p>
	 *
	 * Using a <classname>Segment</classname> is generally more
	 * efficient than using a <classname>String</classname> because it
	 * results in less memory allocation and array copying.<p>
	 *
	 * This method is thread-safe.
	 *
	 * @param line The line
	 * @since jEdit 4.0pre1
	 */
public void getLineText(int line, Segment segment) {
    if (line < 0 || line >= lineMgr.getLineCount())
        throw new ArrayIndexOutOfBoundsException(line);
    try {
        readLock();
        int start = line == 0 ? 0 : lineMgr.getLineEndOffset(line - 1);
        int end = lineMgr.getLineEndOffset(line);
        getText(start, end - start - 1, segment);
    } finally {
        readUnlock();
    }
}
