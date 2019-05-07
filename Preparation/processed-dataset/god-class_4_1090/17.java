//}}}  
//{{{ getLineStartOffset() method  
/**
	 * Returns the start offset of the specified line.
	 * This method is thread-safe.
	 * @param line The line
	 * @return The start offset of the specified line
	 * @since jEdit 4.0pre1
	 */
public int getLineStartOffset(int line) {
    try {
        readLock();
        if (line < 0 || line >= lineMgr.getLineCount())
            throw new ArrayIndexOutOfBoundsException(line);
        else if (line == 0)
            return 0;
        return lineMgr.getLineEndOffset(line - 1);
    } finally {
        readUnlock();
    }
}
