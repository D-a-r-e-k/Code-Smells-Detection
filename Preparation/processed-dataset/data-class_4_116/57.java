/**
	 * Clean delta^2 information about added and deleted.
	 * It also empty the event list.
	 *  
	 * It is called by Marauroa, don't use :)
	 */
public void resetAddedAndDeleted() {
    resetAddedAndDeletedAttributes();
    resetAddedAndDeletedRPSlot();
    resetAddedAndDeletedRPLink();
    resetAddedAndDeletedMaps();
    clearEvents();
    if (modified) {
        modified = false;
    }
}
