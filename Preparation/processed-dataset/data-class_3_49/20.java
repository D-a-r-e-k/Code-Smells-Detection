/**
     * Returns a collection view of the mappings contained in this map.
     * Each element in the returned collection is a <tt>Map.Entry</tt>.  The
     * collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa.  The collection supports element
     * removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the mappings contained in this map.
     */
public Set entrySet() {
    Set es = entrySet;
    if (es != null) {
        return es;
    } else {
        return entrySet = new AbstractSet() {

            public Iterator iterator() {
                return new HashIterator();
            }

            public boolean contains(Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) o;
                Object key = entry.getKey();
                Object v = AbstractConcurrentReadCache.this.get(key);
                return (v != null) && v.equals(entry.getValue());
            }

            public boolean remove(Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                return AbstractConcurrentReadCache.this.findAndRemoveEntry((Map.Entry) o);
            }

            public int size() {
                return AbstractConcurrentReadCache.this.size();
            }

            public void clear() {
                AbstractConcurrentReadCache.this.clear();
            }
        };
    }
}
