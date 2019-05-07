//}}}  
//{{{ getLineLength() method  
/**
	 * Returns the length of the specified line.
	 * This method is thread-safe.
	 * @param line The line
	 * @since jEdit 4.0pre1
	 */
public int getLineLength(int line) {
    try {
        readLock();
        return getLineEndOffset(line) - getLineStartOffset(line) - 1;
    } finally {
        readUnlock();
    }
}
