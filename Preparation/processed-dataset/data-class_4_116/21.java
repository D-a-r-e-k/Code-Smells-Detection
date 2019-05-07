/**
	 * Add an event to this object and set event's owner to this object.
	 *
	 * @param event
	 *            the event to add.
	 */
public void addEvent(RPEvent event) {
    event.setOwner(this);
    events.add(event);
}
