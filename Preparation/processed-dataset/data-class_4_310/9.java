/**
     * Remove stale references from the given list.
     */
private void removeStaleReferences(ReferenceQueue queue, List list) {
    Reference ref = queue.poll();
    int count = 0;
    while (ref != null) {
        ++count;
        ref = queue.poll();
    }
    if (count > 0) {
        final Iterator i = list.iterator();
        while (i.hasNext()) {
            Object o = ((Reference) i.next()).get();
            if (o == null) {
                i.remove();
                if (--count <= 0) {
                    return;
                }
            }
        }
    }
}
