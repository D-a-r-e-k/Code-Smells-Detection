/**
     * Rehashes the contents of this map into a new table with a larger capacity.
     * This method is called automatically when the
     * number of keys in this map exceeds its capacity and load factor.
     */
protected void rehash() {
    Entry[] oldMap = table;
    int oldCapacity = oldMap.length;
    if (oldCapacity >= MAXIMUM_CAPACITY) {
        return;
    }
    int newCapacity = oldCapacity << 1;
    Entry[] newMap = new Entry[newCapacity];
    threshold = (int) (newCapacity * loadFactor);
    /*
          We need to guarantee that any existing reads of oldMap can
          proceed. So we cannot yet null out each oldMap bin.

          Because we are using power-of-two expansion, the elements
          from each bin must either stay at same index, or move
          to oldCapacity+index. We also minimize new node creation by
          catching cases where old nodes can be reused because their
          .next fields won't change. (This is checked only for sequences
          of one and two. It is not worth checking longer ones.)
        */
    for (int i = 0; i < oldCapacity; ++i) {
        Entry l = null;
        Entry h = null;
        Entry e = oldMap[i];
        while (e != null) {
            int hash = e.hash;
            Entry next = e.next;
            if ((hash & oldCapacity) == 0) {
                // stays at newMap[i]  
                if (l == null) {
                    // try to reuse node  
                    if ((next == null) || ((next.next == null) && ((next.hash & oldCapacity) == 0))) {
                        l = e;
                        break;
                    }
                }
                l = new Entry(hash, e.key, e.value, l);
            } else {
                // moves to newMap[oldCapacity+i]  
                if (h == null) {
                    if ((next == null) || ((next.next == null) && ((next.hash & oldCapacity) != 0))) {
                        h = e;
                        break;
                    }
                }
                h = new Entry(hash, e.key, e.value, h);
            }
            e = next;
        }
        newMap[i] = l;
        newMap[oldCapacity + i] = h;
    }
    table = newMap;
    recordModification(newMap);
}
