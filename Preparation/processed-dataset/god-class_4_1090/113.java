//}}}  
//{{{ contentInserted() method  
private void contentInserted(int offset, int length, IntegerArray endOffsets) {
    try {
        transaction = true;
        int startLine = lineMgr.getLineOfOffset(offset);
        int numLines = endOffsets.getSize();
        if (!loading) {
            firePreContentInserted(startLine, offset, numLines, length);
        }
        lineMgr.contentInserted(startLine, offset, numLines, length, endOffsets);
        positionMgr.contentInserted(offset, length);
        setDirty(true);
        if (!loading) {
            fireContentInserted(startLine, offset, numLines, length);
            if (!undoInProgress && !insideCompoundEdit())
                fireTransactionComplete();
        }
    } finally {
        transaction = false;
    }
}
