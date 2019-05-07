//}}}  
//{{{ getVirtualWidth() method  
/**
	 * Returns the virtual column number (taking tabs into account) of the
	 * specified position.
	 *
	 * @param line The line number
	 * @param column The column number
	 * @since jEdit 4.1pre1
	 */
public int getVirtualWidth(int line, int column) {
    try {
        readLock();
        int start = getLineStartOffset(line);
        Segment seg = new Segment();
        getText(start, column, seg);
        return StandardUtilities.getVirtualWidth(seg, getTabSize());
    } finally {
        readUnlock();
    }
}
