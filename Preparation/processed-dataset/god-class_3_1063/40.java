/**
     * Extract some of the elements in the given collection to an
     * ArrayList.  This method synchronizes on the given collection's
     * monitor.  The returned list will never contain more than the
     * specified maximum number of elements.
     * 
     * @param c    the collection whose elements to extract
     * @param max  the maximum number of elements to extract
     * @return  the extraction
     */
private static <T> ArrayList<T> extractSome(Collection<T> c, int max) {
    // Try to guess a sane initial capacity for ArrayList 
    // Hopefully given collection won't grow more than 10 items 
    // between now and the synchronized block... 
    int initial = Math.min(c.size() + 10, max);
    int count = 0;
    ArrayList<T> list = new ArrayList<T>(initial);
    synchronized (c) {
        Iterator<T> iter = c.iterator();
        while (iter.hasNext() && (count < max)) {
            list.add(iter.next());
            count++;
        }
    }
    return list;
}
