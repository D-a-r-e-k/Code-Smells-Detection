/**
	 * Sets the attributes that define the ID of the object.
	 *
	 * @param id
	 *            the object id to set for this object
	 */
public void setID(RPObject.ID id) {
    put("id", id.getObjectID());
    /*
		 * We don't use zoneid inside slots.
		 */
    if (id.getZoneID() != null) {
        put("zoneid", id.getZoneID());
    }
}
