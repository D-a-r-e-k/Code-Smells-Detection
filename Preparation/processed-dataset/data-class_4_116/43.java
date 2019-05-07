/**
	 * removes an entry from a map
	 *
	 * @param map the name of the map
	 * @param key the key of the entry to remove
	 */
public void remove(String map, String key) {
    if ((key.equals("id") || key.equals("zoneid"))) {
        throw new IllegalArgumentException("\"id\" and \"zoneid\" are reserved keys that may not be used");
    }
    if (maps.containsKey(map)) {
        this.maps.get(map).remove(key);
        this.modified = true;
        if (!this.deletedMaps.contains(map)) {
            this.deletedMaps.add(map);
        }
    }
}
