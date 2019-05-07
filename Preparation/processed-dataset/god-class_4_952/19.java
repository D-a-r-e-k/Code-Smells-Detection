/**
	 * This method add the slot to the object
	 *
	 * @param slot
	 *            the RPSlot to be added
	 * @throws SlotAlreadyAddedException
	 *             if the slot already exists
	 */
@Override
public void addSlot(RPSlot slot) throws SlotAlreadyAddedException {
    super.addSlot(slot);
    /* Notify delta^2 about the addition of this slot */
    addedSlots.add(slot.getName());
    modified = true;
}
