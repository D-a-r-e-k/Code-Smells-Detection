/**
	 * With the diferences computed by getDifferences in added and deleted we
	 * build an update object by applying the changes.
	 *
	 * @param addedChanges
	 *            the added and modified attributes, slots and events or null
	 * @param deletedChanges
	 *            the deleted attributes and slots or null
	 */
public void applyDifferences(RPObject addedChanges, RPObject deletedChanges) {
    if (deletedChanges != null) {
        /*
			 * We remove attributes stored in deleted Changes. Except they are
			 * id or zoneid
			 */
        for (String attrib : deletedChanges) {
            if (!attrib.equals("id") && !attrib.equals("zoneid")) {
                remove(attrib);
            }
        }
        /*
			 * We apply the deleted changes to the object of the link.
			 */
        for (RPLink link : deletedChanges.links) {
            if (link.getObject().isEmpty()) {
                removeLink(link.getName());
            } else {
                getLinkedObject(link.getName()).applyDifferences(null, link.getObject());
            }
        }
        /*
			 * we apply the deleted changes to each map 
			 */
        for (Entry<String, RPObject> entry : deletedChanges.maps.entrySet()) {
            if (entry.getValue().isEmpty()) {
                removeMap(entry.getKey());
            } else {
                maps.get(entry.getKey()).applyDifferences(null, entry.getValue());
            }
        }
        /*
			 * Now we move to slots and remove the slot if it is empty on delete
			 * changes.
			 */
        for (RPSlot slot : deletedChanges.slots) {
            if (slot.size() == 0) {
                removeSlot(slot.getName());
            } else {
                RPSlot changes = getSlot(slot.getName());
                /*
					 * For each of the deletded changes, check if they are
					 * already on the object so they an update and recursively
					 * apply differences to it. On the other hand if object is
					 * not present, it means it is a new object so we can add it
					 * directly.
					 */
                for (RPObject del : slot) {
                    /*
						 * If object to remove has more than one attribute that
						 * means that we want to remove these attributes. On the
						 * other hand, if only one attribute is there, that
						 * means that we want to remove the full object from the
						 * slot.
						 */
                    if (del.size() > 1) {
                        RPObject recChanges = changes.get(del.getID());
                        recChanges.applyDifferences(null, del);
                    } else {
                        changes.remove(del.getID());
                    }
                }
            }
        }
    }
    if (addedChanges != null) {
        /*
			 * We add the attributes contained at added changes.
			 */
        for (String attrib : addedChanges) {
            put(attrib, addedChanges.get(attrib));
        }
        /*
			 * We add also the events
			 */
        for (RPEvent event : addedChanges.events) {
            events.add(event);
        }
        /*
			 * We apply it for the links.
			 */
        for (RPLink link : addedChanges.links) {
            if (!hasLink(link.getName())) {
                links.add(link);
            } else {
                getLinkedObject(link.getName()).applyDifferences(link.getObject(), null);
            }
        }
        /*
			 * we apply the added changes for the maps
			 */
        for (Entry<String, RPObject> entry : addedChanges.maps.entrySet()) {
            if (!maps.containsKey(entry.getKey())) {
                maps.put(entry.getKey(), entry.getValue());
            } else {
                maps.get(entry.getKey()).applyDifferences(entry.getValue(), null);
            }
        }
        /*
			 * For each of the added slots we add it and any object that was
			 * inside.
			 */
        for (RPSlot slot : addedChanges.slots) {
            if (!hasSlot(slot.getName())) {
                addSlot(slot.getName());
            }
            RPSlot changes = getSlot(slot.getName());
            /*
				 * For each of the added changes, check if they are already on
				 * the object so they an update and recursively apply
				 * differences to it. On the other hand if object is not
				 * present, it means it is a new object so we can add it
				 * directly.
				 */
            for (RPObject ad : slot) {
                RPObject recChanges = changes.get(ad.getID());
                if (recChanges != null) {
                    recChanges.applyDifferences(ad, null);
                } else {
                    changes.add(ad, false);
                }
            }
        }
    }
}
