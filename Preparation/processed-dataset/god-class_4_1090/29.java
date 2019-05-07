//}}}  
//{{{ remove() method  
/**
	 * Removes the specified rang efrom the buffer.
	 * @param offset The start offset
	 * @param length The number of characters to remove
	 */
public void remove(int offset, int length) {
    if (length == 0)
        return;
    if (isReadOnly())
        throw new RuntimeException("buffer read-only");
    try {
        transaction = true;
        writeLock();
        if (offset < 0 || length < 0 || offset + length > contentMgr.getLength())
            throw new ArrayIndexOutOfBoundsException(offset + ":" + length);
        int startLine = lineMgr.getLineOfOffset(offset);
        int endLine = lineMgr.getLineOfOffset(offset + length);
        int numLines = endLine - startLine;
        if (!undoInProgress && !loading) {
            undoMgr.contentRemoved(offset, length, getText(offset, length), !dirty);
        }
        firePreContentRemoved(startLine, offset, numLines, length);
        contentMgr.remove(offset, length);
        lineMgr.contentRemoved(startLine, offset, numLines, length);
        positionMgr.contentRemoved(offset, length);
        setDirty(true);
        fireContentRemoved(startLine, offset, numLines, length);
        /* otherwise it will be delivered later */
        if (!undoInProgress && !insideCompoundEdit())
            fireTransactionComplete();
    } finally {
        transaction = false;
        writeUnlock();
    }
}
