/**
     * Get an object from the registry.
     *
     * @param key    Array of strings, the unique key.
     * @return value Object stored under the key, or null if none.
     */
public final Object get(String[] key) {
    if (key == null) {
        throw new IllegalArgumentException("null key");
    }
    if (registry.containsKey(key)) {
        return registry.get(key);
    } else {
        return null;
    }
}
