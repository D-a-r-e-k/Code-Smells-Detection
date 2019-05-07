/**
	 * Set added objects in slots for this object and fill object passed as
	 * param. * It is called by Marauroa, don't use :)
	 *
	 * @param object
	 *            the object to fill with added data.
	 */
public void setAddedRPSlot(RPObject object) {
    for (String slot : object.addedSlots) {
        addSlot(slot);
    }
}
