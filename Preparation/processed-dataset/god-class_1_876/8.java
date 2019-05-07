/**
     * Remove an item from the registry.
     *
     * @param key   Array of Strings.
     * @return      The value associated with the key, or null if none.
     */
public final Object remove(String[] key) {
    if (key == null) {
        throw new IllegalArgumentException("null key");
    }
    return registry.remove(key);
}
