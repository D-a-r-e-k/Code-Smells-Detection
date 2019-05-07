/**
     * Remove stale iterator references from the iterator list.
     */
private void removeStaleIteratorReferences() {
    removeStaleReferences(iteratorReferenceQueue, iterators);
}
