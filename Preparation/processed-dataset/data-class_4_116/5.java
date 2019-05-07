/**
	 * Makes this object invisible, so it is not added in any perception. This
	 * method is not callable directly from the object once it has been added to
	 * a zone. If it is already added, this method must be called from
	 * IRPZone.hide()
	 */
public void hide() {
    hidden = true;
}
