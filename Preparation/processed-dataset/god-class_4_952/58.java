/**
	 * Clean delta^2 data in the slots. It is called by Marauroa, don't use :)
	 */
public void resetAddedAndDeletedRPSlot() {
    for (RPSlot slot : slots) {
        slot.resetAddedAndDeletedRPObjects();
        for (RPObject object : slot) {
            object.resetAddedAndDeleted();
        }
    }
    if (modified) {
        addedSlots.clear();
        deletedSlots.clear();
    }
}
