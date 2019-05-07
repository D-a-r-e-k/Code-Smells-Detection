/**
	 * This method sets the object name of the object being observed.
	 *
	 * @param object The ObjectName of the object to be observed.
	 *
	 * @throws IllegalArgumentException.
	 */
public void setObservedObject(ObjectName object) throws IllegalArgumentException {
    if (object == null) {
        throw new IllegalArgumentException("ObjectName cannot be null");
    }
    observedObject = object;
}
