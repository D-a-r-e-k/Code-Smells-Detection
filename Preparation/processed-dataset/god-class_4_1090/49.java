//}}}  
//{{{ createPosition() method  
/**
	 * Creates a floating position.
	 * @param offset The offset
	 */
public Position createPosition(int offset) {
    try {
        readLock();
        if (offset < 0 || offset > contentMgr.getLength())
            throw new ArrayIndexOutOfBoundsException(offset);
        return positionMgr.createPosition(offset);
    } finally {
        readUnlock();
    }
}
