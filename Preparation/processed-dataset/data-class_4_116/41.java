/**
	 * Remove a map from this RPObject
	 * 
	 * @param map the name of the map to remove
	 * @return the RPObject representing the map or null if map not existing
	 */
public RPObject removeMap(String map) {
    if (maps.containsKey(map)) {
        RPObject rpo = maps.get(map);
        this.deletedMaps.add(map);
        modified = true;
        return rpo;
    }
    return null;
}
