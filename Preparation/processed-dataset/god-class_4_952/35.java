/**
	 * Checks if a map has an entry
	 * 
	 * @param map the name of the map to search in
	 * @param key the key to search for
	 * @return true, if the entry exists; false otherwise
	 */
public boolean has(String map, String key) {
    if (!this.maps.containsKey(map)) {
        return false;
    }
    return this.maps.get(map).has(key);
}
