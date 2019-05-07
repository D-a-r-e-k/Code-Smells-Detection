/**
	 * Retrieves a full map with the given name
	 * 
	 * @param map the name of the map
	 * @return a copy of the map or null if no map with the given name is present
	 */
public Map<String, String> getMap(String map) {
    if (this.maps.containsKey(map)) {
        HashMap<String, String> newMap = new HashMap<String, String>();
        RPObject rpObject = this.maps.get(map);
        for (String key : rpObject) {
            if ((!key.equals("id") && !key.equals("zoneid"))) {
                newMap.put(key, rpObject.get(key));
            }
        }
        return newMap;
    }
    return null;
}
