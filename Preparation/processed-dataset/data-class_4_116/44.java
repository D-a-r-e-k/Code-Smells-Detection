/**
	 * gets all maps and their names as a map
	 * 
	 * @return a map with key name of map and value the map itself
	 */
public Map<String, Map<String, String>> maps() {
    Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
    for (String map : this.maps.keySet()) {
        maps.put(map, getMap(map));
    }
    return maps;
}
