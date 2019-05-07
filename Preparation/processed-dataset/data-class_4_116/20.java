/**
	 * This method is used to remove an slot of the object
	 *
	 * @param name
	 *            the name of the slot
	 * @return the removed slot if it is found or null if it is not found.
	 */
@Override
public RPSlot removeSlot(String name) {
    for (Iterator<RPSlot> it = slots.iterator(); it.hasNext(); ) {
        RPSlot slot = it.next();
        if (name.equals(slot.getName())) {
            // BUG: if an slot is added and deleted on the same turn it 
            // shouldn't be mention on deleted. 
            /** Notify delta^2 about the removal of this slot. */
            deletedSlots.add(name);
            modified = true;
            /* Remove and return it */
            it.remove();
            return slot;
        }
    }
    return null;
}
