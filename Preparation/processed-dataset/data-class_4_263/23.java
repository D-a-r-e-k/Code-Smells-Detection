//}}}  
//{{{ getLineSegment() method  
/**
	 * Returns the text on the specified line.
	 * This method is thread-safe.
	 *
	 * @param line The line index.
	 * @return The text, or null if the line is invalid
	 *
	 * @since jEdit 4.3pre15
	 */
public CharSequence getLineSegment(int line) {
    if (line < 0 || line >= lineMgr.getLineCount())
        throw new ArrayIndexOutOfBoundsException(line);
    try {
        readLock();
        int start = line == 0 ? 0 : lineMgr.getLineEndOffset(line - 1);
        int end = lineMgr.getLineEndOffset(line);
        return getSegment(start, end - start - 1);
    } finally {
        readUnlock();
    }
}
