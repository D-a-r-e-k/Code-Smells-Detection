//}}}  
//{{{ getLineOfOffset() method  
/**
	 * Returns the line containing the specified offset.
	 * This method is thread-safe.
	 * @param offset The offset
	 * @since jEdit 4.0pre1
	 */
public int getLineOfOffset(int offset) {
    try {
        readLock();
        if (offset < 0 || offset > getLength())
            throw new ArrayIndexOutOfBoundsException(offset);
        return lineMgr.getLineOfOffset(offset);
    } finally {
        readUnlock();
    }
}
