/**
     * Returns a shallow copy of this.
     * <tt>AbstractConcurrentReadCache</tt> instance: the keys and
     * values themselves are not cloned.
     *
     * @return a shallow copy of this map.
     */
public synchronized Object clone() {
    try {
        AbstractConcurrentReadCache t = (AbstractConcurrentReadCache) super.clone();
        t.keySet = null;
        t.entrySet = null;
        t.values = null;
        Entry[] tab = table;
        t.table = new Entry[tab.length];
        Entry[] ttab = t.table;
        for (int i = 0; i < tab.length; ++i) {
            Entry first = tab[i];
            if (first != null) {
                ttab[i] = (Entry) (first.clone());
            }
        }
        return t;
    } catch (CloneNotSupportedException e) {
        // this shouldn't happen, since we are Cloneable  
        throw new InternalError();
    }
}
