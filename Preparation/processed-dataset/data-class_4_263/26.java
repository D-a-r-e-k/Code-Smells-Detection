//}}}  
//{{{ getSegment() method  
/**
	 * Returns the specified text range. This method is thread-safe.
	 *
	 * @param start The start offset
	 * @param length The number of characters to get
	 *
	 * @since jEdit 4.3pre15
	 */
public CharSequence getSegment(int start, int length) {
    try {
        readLock();
        if (start < 0 || length < 0 || start + length > contentMgr.getLength())
            throw new ArrayIndexOutOfBoundsException(start + ":" + length);
        return contentMgr.getSegment(start, length);
    } finally {
        readUnlock();
    }
}
