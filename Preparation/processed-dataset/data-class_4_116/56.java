/**
	 * Create a depth copy of the object
	 *
	 * @return a copy of this object.
	 */
@Override
public Object clone() {
    try {
        RPObject rpobject = (RPObject) super.clone();
        rpobject.clear();
        rpobject.fill(this);
        return rpobject;
    } catch (CloneNotSupportedException e) {
        logger.error(e, e);
        return null;
    }
}
