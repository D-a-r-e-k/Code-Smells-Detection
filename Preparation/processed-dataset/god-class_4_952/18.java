/**
	 * This method add the slot to the object
	 *
	 * @param name
	 *            the RPSlot name to be added
	 * @throws SlotAlreadyAddedException
	 *             if the slot already exists
	 */
@Override
public void addSlot(String name) throws SlotAlreadyAddedException {
    super.addSlot(name);
    /** Notify delta^2 about the addition of this slot */
    addedSlots.add(name);
    modified = true;
}
