/**
	 * Returns the base container where this object is
	 *
	 * @return the base container of this object.
	 */
public RPObject getBaseContainer() {
    if (container != null) {
        return (RPObject) container.getContainerBaseOwner();
    } else {
        return this;
    }
}
