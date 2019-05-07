/**
	 * Makes this object visible again. This method is not callable directly
	 * from the object once it has been added to a zone. If it is already added,
	 * this method must be called from IRPZone.unhide()
	 */
public void unhide() {
    hidden = false;
}
