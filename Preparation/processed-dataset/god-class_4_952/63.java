/**
	 * adds the maps added in the specified object as maps to this object
	 *
	 * @param object RPObject to copy the added maps from
	 */
public void setAddedMaps(RPObject object) {
    for (String map : object.addedMaps) {
        addMap(map);
    }
}
