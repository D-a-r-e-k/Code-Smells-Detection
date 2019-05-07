private void notifyRangesReplacedText(CharacterDataImpl node) {
    removeStaleRangeReferences();
    final Iterator i = ranges.iterator();
    while (i.hasNext()) {
        RangeImpl range = (RangeImpl) ((Reference) i.next()).get();
        if (range != null) {
            range.receiveReplacedText(node);
        } else {
            i.remove();
        }
    }
}
