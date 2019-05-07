/**
	 * Removes the visible attributes and events from this object. It iterates
	 * through the slots to remove the attributes too of the contained objects
	 * if they are empty.
	 * @param sync keep the structure intact, by not removing empty slots and links.
	 */
@Override
public void clearVisible(boolean sync) {
    super.clearVisible(sync);
    Iterator<RPEvent> eventsit = events.iterator();
    while (eventsit.hasNext()) {
        /* Iterate over events and remove all of them that are visible */
        RPEvent event = eventsit.next();
        Definition def = getRPClass().getDefinition(DefinitionClass.RPEVENT, event.getName());
        if (def == null) {
            logger.warn("Null Definition for event: " + event.getName() + " of RPClass: " + getRPClass().getName());
            continue;
        }
        if (def.isVisible()) {
            eventsit.remove();
        }
    }
    Iterator<RPSlot> slotit = slots.iterator();
    while (slotit.hasNext()) {
        RPSlot slot = slotit.next();
        slot.clearVisible(sync);
        /*
			 * Even if slot is empty client may be interested in knowing the slot.
			 * So we don't remove the slot on sync type of clear visible.
			 */
        if (!sync && slot.size() == 0) {
            slotit.remove();
            addedSlots.remove(slot.getName());
            deletedSlots.remove(slot.getName());
            modified = true;
        }
    }
    Iterator<RPLink> linkit = links.iterator();
    while (linkit.hasNext()) {
        RPLink link = linkit.next();
        link.getObject().clearVisible(sync);
        /* If link is empty remove it. */
        if (link.getObject().isEmpty()) {
            linkit.remove();
            addedLinks.remove(link.getName());
            deletedLinks.remove(link.getName());
            modified = true;
        }
    }
}
