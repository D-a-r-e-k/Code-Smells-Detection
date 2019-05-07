/**
     * Add an object to the registry, overwriting any existing value.
     *
     * @param key   Array of Strings, the unique key.  Must not be null.
     * @param value Object to be stored under this key.
     * @return      Old value if there was one, null otherwise.
     */
public final Object put(String[] key, Object value) {
    if (key == null) {
        throw new IllegalArgumentException("null key");
    }
    // make a copy of the key 
    String keyCopy[] = new String[key.length];
    for (int i = 0; i < key.length; i++) {
        keyCopy[i] = key[i];
    }
    return registry.put(keyCopy, value);
}
