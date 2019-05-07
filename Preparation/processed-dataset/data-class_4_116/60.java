/**
	 * Clean delta^2 data in the maps. It is called by Marauroa, don't use :)
	 */
public void resetAddedAndDeletedMaps() {
    for (RPObject map : maps.values()) {
        map.resetAddedAndDeleted();
    }
    addedMaps.clear();
    deletedMaps.clear();
}
