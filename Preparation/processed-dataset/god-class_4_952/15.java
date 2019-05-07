/**
	 * Returns the base container where this object is
	 *
	 * @return the base container of this object.
	 */
@Override
public SlotOwner getContainerBaseOwner() {
    if (container != null) {
        return container.getContainerBaseOwner();
    } else {
        return this;
    }
}
