private void notifyRangesDeletedText(CharacterDataImpl node, int offset, int count) {
    removeStaleRangeReferences();
    final Iterator i = ranges.iterator();
    while (i.hasNext()) {
        RangeImpl range = (RangeImpl) ((Reference) i.next()).get();
        if (range != null) {
            range.receiveDeletedText(node, offset, count);
        } else {
            i.remove();
        }
    }
}
