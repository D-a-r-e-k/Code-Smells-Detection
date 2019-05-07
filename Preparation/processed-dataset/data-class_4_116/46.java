/**
	 * check if a map of this object contains a key
	 * @param map the name of the map
	 * @param key the key to check for
	 * @return true iff map has a value stored for key
	 */
public boolean containsKey(String map, String key) {
    if (hasMap(map)) {
        return getMap(map).containsKey(key);
    }
    return false;
}
