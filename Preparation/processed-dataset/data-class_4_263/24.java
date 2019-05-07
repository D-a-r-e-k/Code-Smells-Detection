//}}}  
//{{{ getText() methods  
/**
	 * Returns the specified text range. This method is thread-safe.
	 * @param start The start offset
	 * @param length The number of characters to get
	 */
public String getText(int start, int length) {
    try {
        readLock();
        if (start < 0 || length < 0 || start + length > contentMgr.getLength())
            throw new ArrayIndexOutOfBoundsException(start + ":" + length);
        return contentMgr.getText(start, length);
    } finally {
        readUnlock();
    }
}
