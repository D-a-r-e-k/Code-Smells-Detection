//}}}  
//{{{ loadText() method  
protected void loadText(Segment seg, IntegerArray endOffsets) {
    if (seg == null)
        seg = new Segment(new char[1024], 0, 0);
    if (endOffsets == null) {
        endOffsets = new IntegerArray();
        endOffsets.add(1);
    }
    try {
        writeLock();
        // For `reload' command  
        // contentMgr.remove() changes this!  
        int length = getLength();
        firePreContentRemoved(0, 0, getLineCount() - 1, length);
        contentMgr.remove(0, length);
        lineMgr.contentRemoved(0, 0, getLineCount() - 1, length);
        positionMgr.contentRemoved(0, length);
        fireContentRemoved(0, 0, getLineCount() - 1, length);
        firePreContentInserted(0, 0, endOffsets.getSize() - 1, seg.count - 1);
        // theoretically a segment could  
        // have seg.offset != 0 but  
        // SegmentBuffer never does that  
        contentMgr._setContent(seg.array, seg.count);
        lineMgr._contentInserted(endOffsets);
        positionMgr.contentInserted(0, seg.count);
        fireContentInserted(0, 0, endOffsets.getSize() - 1, seg.count - 1);
    } finally {
        writeUnlock();
    }
}
