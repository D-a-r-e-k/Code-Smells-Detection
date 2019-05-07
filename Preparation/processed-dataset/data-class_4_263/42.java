//}}}  
//{{{ insertAtColumn() method  
/**
	 * Like the {@link #insert(int,String)} method, but inserts the string at
	 * the specified virtual column. Inserts spaces as appropriate if
	 * the line is shorter than the column.
	 * @param line The line number
	 * @param col The virtual column number
	 * @param str The string
	 */
public void insertAtColumn(int line, int col, String str) {
    try {
        writeLock();
        int[] total = new int[1];
        int offset = getOffsetOfVirtualColumn(line, col, total);
        if (offset == -1) {
            offset = getLineEndOffset(line) - 1;
            str = StandardUtilities.createWhiteSpace(col - total[0], 0) + str;
        } else
            offset += getLineStartOffset(line);
        insert(offset, str);
    } finally {
        writeUnlock();
    }
}
