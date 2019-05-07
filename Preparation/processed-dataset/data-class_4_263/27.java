//}}}  
//{{{ insert() methods  
/**
	 * Inserts a string into the buffer.
	 * @param offset The offset
	 * @param str The string
	 * @since jEdit 4.0pre1
	 */
public void insert(int offset, String str) {
    if (str == null)
        return;
    int len = str.length();
    if (len == 0)
        return;
    if (isReadOnly())
        throw new RuntimeException("buffer read-only");
    try {
        writeLock();
        if (offset < 0 || offset > contentMgr.getLength())
            throw new ArrayIndexOutOfBoundsException(offset);
        contentMgr.insert(offset, str);
        integerArray.clear();
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) == '\n')
                integerArray.add(i + 1);
        }
        if (!undoInProgress) {
            undoMgr.contentInserted(offset, len, str, !dirty);
        }
        contentInserted(offset, len, integerArray);
    } finally {
        writeUnlock();
    }
}
