//}}}  
//{{{ getOffsetOfVirtualColumn() method  
/**
	 * Returns the offset of a virtual column number (taking tabs
	 * into account) relative to the start of the line in question.
	 *
	 * @param line The line number
	 * @param column The virtual column number
	 * @param totalVirtualWidth If this array is non-null, the total
	 * virtual width will be stored in its first location if this method
	 * returns -1.
	 *
	 * @return -1 if the column is out of bounds
	 *
	 * @since jEdit 4.1pre1
	 */
public int getOffsetOfVirtualColumn(int line, int column, int[] totalVirtualWidth) {
    try {
        readLock();
        Segment seg = new Segment();
        getLineText(line, seg);
        return StandardUtilities.getOffsetOfVirtualColumn(seg, getTabSize(), column, totalVirtualWidth);
    } finally {
        readUnlock();
    }
}
