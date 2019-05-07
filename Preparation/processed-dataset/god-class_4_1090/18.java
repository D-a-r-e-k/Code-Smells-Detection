//}}}  
//{{{ getLineEndOffset() method  
/**
	 * Returns the end offset of the specified line.
	 * This method is thread-safe.
	 * @param line The line
	 * @return The end offset of the specified line
	 * invalid.
	 * @since jEdit 4.0pre1
	 */
public int getLineEndOffset(int line) {
    try {
        readLock();
        if (line < 0 || line >= lineMgr.getLineCount())
            throw new ArrayIndexOutOfBoundsException(line);
        return lineMgr.getLineEndOffset(line);
    } finally {
        readUnlock();
    }
}
