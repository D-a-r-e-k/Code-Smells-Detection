/**
	 * Retrieve the differences stored in this object and add them to two new
	 * objects added changes and delete changes that will contains added and
	 * modified attributes, slots and events and on the other hand deleted
	 * changes that will contain the removes slots and attributes. We don't care
	 * about RP Events because they are removed on each turn.
	 *
	 * @param addedChanges
	 *            an empty object
	 * @param deletedChanges
	 *            an empty object
	 */
public void getDifferences(RPObject addedChanges, RPObject deletedChanges) {
    /*
		 * First we get the diff from attributes this object contains.
		 */
    addedChanges.setAddedAttributes(this);
    deletedChanges.setDeletedAttributes(this);
    /*
		 * We add to the added object the events that exists.
		 * Because events are cleared on each turn so they have no delta^2
		 */
    for (RPEvent event : events) {
        addedChanges.events.add(event);
    }
    /*
		 * We add the added links.
		 */
    for (String addedLink : addedLinks) {
        addedChanges.addLink(addedLink, getLinkedObject(addedLink));
    }
    /*
		 * We add the deleted links.
		 */
    for (String deletedLink : deletedLinks) {
        deletedChanges.addLink(deletedLink, new RPObject());
    }
    /*
		 * We now get the diffs for the links
		 */
    for (RPLink link : links) {
        RPObject linkadded = new RPObject();
        RPObject linkdeleted = new RPObject();
        link.getObject().getDifferences(linkadded, linkdeleted);
        if (!linkadded.isEmpty()) {
            addedChanges.addLink(link.getName(), linkadded);
        }
        if (!linkdeleted.isEmpty()) {
            deletedChanges.addLink(link.getName(), linkdeleted);
        }
    }
    /*
		 * Now we get the diff from slots.
		 */
    addedChanges.setAddedRPSlot(this);
    deletedChanges.setDeletedRPSlot(this);
    for (RPSlot slot : slots) {
        // ignore all slots that are server only 
        Definition def = this.getRPClass().getDefinition(DefinitionClass.RPSLOT, slot.getName());
        if (def.isHidden()) {
            continue;
        }
        /*
			 * First we process the added things to slot.
			 */
        RPSlot addedObjectsInSlot = new RPSlot(slot.getName());
        if (addedObjectsInSlot.setAddedRPObject(slot)) {
            /*
				 * There is added objects in the slot, so we need to add them to
				 * addedChanges.
				 */
            if (!addedChanges.hasSlot(slot.getName())) {
                addedChanges.addSlot(slot.getName());
            }
            RPSlot changes = addedChanges.getSlot(slot.getName());
            for (RPObject ad : addedObjectsInSlot) {
                changes.add(ad, false);
            }
        }
        /*
			 * Later we process the removed things from the slot.
			 */
        RPSlot deletedObjectsInSlot = new RPSlot(slot.getName());
        if (deletedObjectsInSlot.setDeletedRPObject(slot)) {
            /*
				 * There is deleted objects in the slot, so we need to add them
				 * to deletedChanges.
				 */
            if (!deletedChanges.hasSlot(slot.getName())) {
                deletedChanges.addSlot(slot.getName());
            }
            RPSlot changes = deletedChanges.getSlot(slot.getName());
            for (RPObject ad : deletedObjectsInSlot) {
                changes.add(ad, false);
            }
        }
        /*
			 * Finally we process the changes on the objects of the slot.
			 */
        for (RPObject rec : slot) {
            // ignore modified objects that has been added in the same turn 
            if (!addedObjectsInSlot.has(rec.getID())) {
                RPObject recAddedChanges = new RPObject();
                RPObject recDeletedChanges = new RPObject();
                rec.getDifferences(recAddedChanges, recDeletedChanges);
                /*
					 * If this object is not empty that means that there has been a
					 * change at it. So we add this object to the slot.
					 */
                if (!recAddedChanges.isEmpty()) {
                    /*
						 * If slot was not created, create it now. For example if an
						 * object is modified ( that means not added nor deleted ),
						 * it won't have a slot already created on added.
						 */
                    if (!addedChanges.hasSlot(slot.getName())) {
                        addedChanges.addSlot(slot.getName());
                    }
                    RPSlot recAddedSlot = addedChanges.getSlot(slot.getName());
                    /*
						 * We need to set the id of the object to be equals to the
						 * object from which the diff was generated.
						 */
                    recAddedChanges.put("id", rec.get("id"));
                    recAddedSlot.add(recAddedChanges, false);
                }
                /*
					 * Same operation with delete changes
					 */
                if (!recDeletedChanges.isEmpty()) {
                    /*
						 * If slot was not created, create it now. For example if an
						 * object is modified ( that means not added nor deleted ),
						 * it won't have a slot already created on added.
						 */
                    if (!deletedChanges.hasSlot(slot.getName())) {
                        deletedChanges.addSlot(slot.getName());
                    }
                    RPSlot recDeletedSlot = deletedChanges.getSlot(slot.getName());
                    /*
						 * We need to set the id of the object to be equals to the
						 * object from which the diff was generated.
						 */
                    recDeletedChanges.put("id", rec.get("id"));
                    recDeletedSlot.add(recDeletedChanges, false);
                }
            }
        }
    }
    /*
		 * now we deal with the diff of maps
		 */
    addedChanges.setAddedMaps(this);
    deletedChanges.setDeletedMaps(this);
    /*
		 * We now get the diffs for the maps
		 */
    for (Entry<String, RPObject> entry : maps.entrySet()) {
        RPObject addedMapChanges = new RPObject();
        RPObject deletedMapChanges = new RPObject();
        entry.getValue().getDifferences(addedMapChanges, deletedMapChanges);
        if (!addedMapChanges.isEmpty()) {
            for (String attribute : addedMapChanges) {
                if (!attribute.equals("id") && !attribute.equals("zoneid")) {
                    addedChanges.put(entry.getKey(), attribute, addedMapChanges.get(attribute));
                }
            }
        }
        if (!deletedMapChanges.isEmpty()) {
            for (String attribute : deletedMapChanges) {
                if (!attribute.equals("id") && !attribute.equals("zoneid")) {
                    deletedChanges.put(entry.getKey(), attribute, deletedMapChanges.get(attribute));
                }
            }
        }
    }
    /*
		 * If the diff objects are not empty, we make sure they has the id.
		 */
    if (!addedChanges.isEmpty()) {
        addedChanges.put("id", get("id"));
        if (has("zoneid")) {
            addedChanges.put("zoneid", get("zoneid"));
        }
    }
    if (!deletedChanges.isEmpty()) {
        deletedChanges.put("id", get("id"));
        if (has("zoneid")) {
            deletedChanges.put("zoneid", get("zoneid"));
        }
    }
}
