/**
     * Returns a set view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are reflected in the set, and
     * vice-versa.  The set supports element removal, which removes the
     * corresponding mapping from this map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
     * <tt>clear</tt> operations.  It does not support the <tt>add</tt> or
     * <tt>addAll</tt> operations.
     *
     * @return a set view of the keys contained in this map.
     */
public Set keySet() {
    Set ks = keySet;
    if (ks != null) {
        return ks;
    } else {
        return keySet = new AbstractSet() {

            public Iterator iterator() {
                return new KeyIterator();
            }

            public int size() {
                return AbstractConcurrentReadCache.this.size();
            }

            public boolean contains(Object o) {
                return AbstractConcurrentReadCache.this.containsKey(o);
            }

            public boolean remove(Object o) {
                return AbstractConcurrentReadCache.this.remove(o) != null;
            }

            public void clear() {
                AbstractConcurrentReadCache.this.clear();
            }
        };
    }
}
