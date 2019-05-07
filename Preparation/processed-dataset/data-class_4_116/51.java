/**
	 * Returns true if two objects are exactly equal
	 *
	 * @param obj
	 *            the object to compare with this one.
	 */
@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj instanceof RPObject) {
        RPObject object = (RPObject) obj;
        return super.equals(obj) && slots.equals(object.slots) && maps.equals(object.maps) && events.equals(object.events) && links.equals(object.links);
    } else {
        return false;
    }
}
