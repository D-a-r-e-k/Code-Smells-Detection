public int sizeEstimate(PersistentStore store) {
    firstRow(null, store);
    return (int) (1L << depth);
}
