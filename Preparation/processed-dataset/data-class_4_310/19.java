private void notifyRangesSplitData(Node node, Node newNode, int offset) {
    removeStaleRangeReferences();
    final Iterator i = ranges.iterator();
    while (i.hasNext()) {
        RangeImpl range = (RangeImpl) ((Reference) i.next()).get();
        if (range != null) {
            range.receiveSplitData(node, newNode, offset);
        } else {
            i.remove();
        }
    }
}
