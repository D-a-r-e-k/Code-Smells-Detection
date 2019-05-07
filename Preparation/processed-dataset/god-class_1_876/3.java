/** @return True if there is an item under this key in the registry. */
public final boolean containsKey(String[] key) {
    if (key == null) {
        throw new IllegalArgumentException("null key");
    }
    return registry.containsKey(key);
}
