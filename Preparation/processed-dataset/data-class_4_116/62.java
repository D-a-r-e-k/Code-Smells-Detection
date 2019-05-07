/**
	 * Set deleted objects in slots for this object and fill object passed as
	 * param. * It is called by Marauroa, don't use :)
	 *
	 * @param object
	 *            the object to fill with deleted data.
	 */
public void setDeletedRPSlot(RPObject object) {
    for (String slot : object.deletedSlots) {
        addSlot(slot);
    }
}
