/** Not a client function. Called by Range.detach(),
     *  so a Range can remove itself from the list of
     *  Ranges.
     */
void removeRange(Range range) {
    if (range == null)
        return;
    if (ranges == null)
        return;
    removeStaleRangeReferences();
    Iterator i = ranges.iterator();
    while (i.hasNext()) {
        Object otherRange = ((Reference) i.next()).get();
        if (otherRange == range) {
            i.remove();
            return;
        } else if (otherRange == null) {
            i.remove();
        }
    }
}
