/**
     * Set the cache capacity
     */
public void setMaxEntries(int newLimit) {
    if (newLimit > 0) {
        maxEntries = newLimit;
        synchronized (this) {
            // because remove() isn't synchronized  
            while (size() > maxEntries) {
                remove(removeItem(), false, false);
            }
        }
    } else {
        // Capacity must be at least 1  
        throw new IllegalArgumentException("Cache maximum number of entries must be at least 1");
    }
}
