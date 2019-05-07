/**
	 * Gets and object from the tree of RPSlots using its id ( that it is unique ).
	 * Return null if it is not found.
	 *  
	 * @param id the id of the object to look for.
	 * @return RPObject
	 */
public RPObject getFromSlots(int id) {
    if (isContained()) {
        if (getInt("id") == id) {
            return this;
        }
    }
    RPObject found = null;
    for (RPSlot slot : slots) {
        for (RPObject object : slot) {
            found = object.getFromSlots(id);
            if (found != null) {
                return found;
            }
        }
    }
    return found;
}
