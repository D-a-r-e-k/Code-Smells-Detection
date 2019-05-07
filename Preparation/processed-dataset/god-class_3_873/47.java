/**
     * Reconstitute the <tt>AbstractConcurrentReadCache</tt>.
     * instance from a stream (i.e.,
     * deserialize it).
     */
private synchronized void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
    // Read in the threshold, loadfactor, and any hidden stuff  
    s.defaultReadObject();
    // Read in number of buckets and allocate the bucket array;  
    int numBuckets = s.readInt();
    table = new Entry[numBuckets];
    // Read in size (number of Mappings)  
    int size = s.readInt();
    // Read the keys and values, and put the mappings in the table  
    for (int i = 0; i < size; i++) {
        Object key = s.readObject();
        Object value = s.readObject();
        put(key, value);
    }
}
