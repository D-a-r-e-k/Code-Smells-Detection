private void notifyRangesInsertedNode(NodeImpl newInternal) {
    removeStaleRangeReferences();
    final Iterator i = ranges.iterator();
    while (i.hasNext()) {
        RangeImpl range = (RangeImpl) ((Reference) i.next()).get();
        if (range != null) {
            range.insertedNodeFromDOM(newInternal);
        } else {
            i.remove();
        }
    }
}
