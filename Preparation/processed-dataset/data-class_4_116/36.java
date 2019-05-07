/**
	 * Retrieves a value
	 * 
	 * @param map the name of the map to search in
	 * @param key the key to search for
	 * @return the value found
	 */
public String get(String map, String key) {
    if (!this.maps.containsKey(map)) {
        Definition def = getRPClass().getDefinition(DefinitionClass.STATIC, map);
        if (def != null) {
            /*
				 * It is possible that the attribute itself doesn't exist as static attribute, 
				 * so we should return null instead.
				 */
            return def.getValue();
        }
        return null;
    }
    return this.maps.get(map).get(key);
}
