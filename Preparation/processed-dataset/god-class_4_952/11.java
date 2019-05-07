/**
	 * This make this object to be contained in the slot of container.
	 *
	 * @param slotOwner
	 *            the object that is going to contain this object.
	 * @param slot
	 *            the slot of the object that contains this object.
	 */
@Override
public void setContainer(SlotOwner slotOwner, RPSlot slot) {
    container = slotOwner;
    containerSlot = slot;
}
