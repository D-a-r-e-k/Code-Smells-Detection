/**
	 * Copy constructor
	 *
	 * @param object
	 *            the object that is going to be copied.
	 */
public void fill(RPObject object) {
    super.fill(object);
    hidden = object.hidden;
    storable = object.storable;
    modified = object.modified;
    container = object.container;
    containerSlot = object.containerSlot;
    for (RPEvent event : object.events) {
        RPEvent added = (RPEvent) event.clone();
        added.setOwner(this);
        events.add(added);
    }
    for (RPLink link : object.links) {
        RPLink added = (RPLink) link.clone();
        added.setOwner(this);
        links.add(added);
    }
    for (Entry<String, RPObject> entry : object.maps.entrySet()) {
        RPObject toAdd = (RPObject) entry.getValue().clone();
        maps.put(entry.getKey(), toAdd);
    }
    /*
		 * Copy also the delta^2 info.
		 */
    for (String slot : object.addedSlots) {
        addedSlots.add(slot);
    }
    for (String slot : object.deletedSlots) {
        deletedSlots.add(slot);
    }
    for (String link : object.addedLinks) {
        addedLinks.add(link);
    }
    for (String link : object.deletedLinks) {
        deletedLinks.add(link);
    }
    for (String entry : object.addedMaps) {
        addedMaps.add(entry);
    }
    for (String entry : object.deletedMaps) {
        deletedMaps.add(entry);
    }
}
