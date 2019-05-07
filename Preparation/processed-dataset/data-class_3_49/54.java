/** OpenSymphony END (pretty long!) */
/**
     * Returns the appropriate capacity (power of two) for the specified
     * initial capacity argument.
     */
private int p2capacity(int initialCapacity) {
    int cap = initialCapacity;
    // Compute the appropriate capacity  
    int result;
    if ((cap > MAXIMUM_CAPACITY) || (cap < 0)) {
        result = MAXIMUM_CAPACITY;
    } else {
        result = MINIMUM_CAPACITY;
        while (result < cap) {
            result <<= 1;
        }
    }
    return result;
}
