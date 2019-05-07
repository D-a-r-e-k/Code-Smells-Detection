/**
     * Gets the name index for a given key.
     *
     * @param key The key to use.
     */
public int getNameIndex(String key) {
    Integer val = nameIndex.get(key);
    return (val == null) ? 0 : val;
}
