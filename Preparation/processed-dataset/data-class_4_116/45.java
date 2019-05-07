/**
	 * check if a map is present in this object
	 * @param map the name of the map
	 * @return true iff this objects has a map with that name
	 */
public boolean hasMap(String map) {
    return this.maps.containsKey(map);
}
