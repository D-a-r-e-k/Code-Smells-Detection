private void notifyRangesRemovingNode(NodeImpl oldChild) {
    removeStaleRangeReferences();
    final Iterator i = ranges.iterator();
    while (i.hasNext()) {
        RangeImpl range = (RangeImpl) ((Reference) i.next()).get();
        if (range != null) {
            range.removeNode(oldChild);
        } else {
            i.remove();
        }
    }
}
