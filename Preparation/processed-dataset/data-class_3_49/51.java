/**
     * Save the state of the <tt>AbstractConcurrentReadCache</tt> instance to a stream.
     * (i.e., serialize it).
     *
     * @serialData The <i>capacity</i> of the
     * AbstractConcurrentReadCache (the length of the
     * bucket array) is emitted (int), followed  by the
     * <i>size</i> of the AbstractConcurrentReadCache (the number of key-value
     * mappings), followed by the key (Object) and value (Object)
     * for each key-value mapping represented by the AbstractConcurrentReadCache
     * The key-value mappings are emitted in no particular order.
     */
private synchronized void writeObject(java.io.ObjectOutputStream s) throws IOException {
    // Write out the threshold, loadfactor, and any hidden stuff  
    s.defaultWriteObject();
    // Write out number of buckets  
    s.writeInt(table.length);
    // Write out size (number of Mappings)  
    s.writeInt(count);
    // Write out keys and values (alternating)  
    for (int index = table.length - 1; index >= 0; index--) {
        Entry entry = table[index];
        while (entry != null) {
            s.writeObject(entry.key);
            s.writeObject(entry.value);
            entry = entry.next;
        }
    }
}
