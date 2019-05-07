/**
	 * adds the maps deleted in the specified object as maps to this object
	 *
	 * @param object RPObject to copy the deleted maps from
	 */
public void setDeletedMaps(RPObject object) {
    for (String map : object.deletedMaps) {
        addMap(map);
    }
}
