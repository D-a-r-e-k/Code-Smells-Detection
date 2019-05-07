/**
	 * Retrieves a value
	 * 
	 * @param map the name of the map to search in
	 * @param key the key to search for
	 * @return the value found
	 */
public boolean getBoolean(String map, String key) {
    if (!this.maps.containsKey(map)) {
        throw new IllegalArgumentException("Map " + map + " not found");
    }
    return this.maps.get(map).getBool(key);
}
