/**
	 * adds a Map to this RPObject
	 * 
	 * @param map
	 */
public void addMap(String map) {
    if (maps.containsKey(map)) {
        throw new SlotAlreadyAddedException(map);
    }
    if (getRPClass() != null) {
        if (getRPClass().getDefinition(DefinitionClass.ATTRIBUTE, map).getType() != Type.MAP) {
            throw new IllegalArgumentException("The type of the attribute " + map + " is not MAP type.");
        }
    }
    RPObject newMap = new RPObject();
    newMap.setID(RPObject.INVALID_ID);
    maps.put(map, newMap);
    addedMaps.add(map);
    modified = true;
}
