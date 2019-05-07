/**
	 * Clean delta^2 data in the links. It is called by Marauroa, don't use :)
	 */
public void resetAddedAndDeletedRPLink() {
    for (RPLink link : links) {
        link.getObject().resetAddedAndDeleted();
    }
    if (modified) {
        addedLinks.clear();
        deletedLinks.clear();
    }
}
