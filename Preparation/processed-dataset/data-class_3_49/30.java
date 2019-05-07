/**
     * Returns a collection view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa.  The collection supports element
     * removal, which removes the corresponding mapping from this map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map.
     */
public Collection values() {
    Collection vs = values;
    if (vs != null) {
        return vs;
    } else {
        return values = new AbstractCollection() {

            public Iterator iterator() {
                return new ValueIterator();
            }

            public int size() {
                return AbstractConcurrentReadCache.this.size();
            }

            public boolean contains(Object o) {
                return AbstractConcurrentReadCache.this.containsValue(o);
            }

            public void clear() {
                AbstractConcurrentReadCache.this.clear();
            }
        };
    }
}
