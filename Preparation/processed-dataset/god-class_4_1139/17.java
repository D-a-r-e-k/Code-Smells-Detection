private void notifyRangesInsertedText(CharacterDataImpl node, int offset, int count) {
    removeStaleRangeReferences();
    final Iterator i = ranges.iterator();
    while (i.hasNext()) {
        RangeImpl range = (RangeImpl) ((Reference) i.next()).get();
        if (range != null) {
            range.receiveInsertedText(node, offset, count);
        } else {
            i.remove();
        }
    }
}
