/**
	 * Puts a value for a key in a given map
	 * 
	 * @param map the name of the map to put in the value
	 * @param key the key to store for the value
	 * @param value the value
	 */
public void put(String map, String key, String value) {
    if (!this.maps.containsKey(map)) {
        this.addMap(map);
    }
    if ((key.equals("id") || key.equals("zoneid"))) {
        throw new IllegalArgumentException("\"id\" and \"zoneid\" are reserved keys that may not be used.");
    }
    this.maps.get(map).put(key, value);
    if (!this.addedMaps.contains(map)) {
        this.addedMaps.add(map);
    }
}
