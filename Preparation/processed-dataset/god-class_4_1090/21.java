//}}}  
//}}}  
//{{{ Text getters and setters  
//{{{ getLineText() methods  
/**
	 * Returns the text on the specified line.
	 * This method is thread-safe.
	 * @param line The line
	 * @return The text, or null if the line is invalid
	 * @since jEdit 4.0pre1
	 */
public String getLineText(int line) {
    if (line < 0 || line >= lineMgr.getLineCount())
        throw new ArrayIndexOutOfBoundsException(line);
    try {
        readLock();
        int start = line == 0 ? 0 : lineMgr.getLineEndOffset(line - 1);
        int end = lineMgr.getLineEndOffset(line);
        return getText(start, end - start - 1);
    } finally {
        readUnlock();
    }
}
