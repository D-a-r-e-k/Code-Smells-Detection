/**
	 * Returns true if the object is empty
	 *
	 * @return true if the object lacks of any attribute, slots, maps or events.
	 */
@Override
public boolean isEmpty() {
    return super.isEmpty() && slots.isEmpty() && events.isEmpty() && links.isEmpty() && maps.isEmpty();
}
