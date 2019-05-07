/**
	 * This method sets the attribute being observed.
	 *
	 * @param attribute The attribute to be observed.
	 */
public void setObservedAttribute(String attribute) throws IllegalArgumentException {
    if (attribute == null || attribute.equals("")) {
        throw new IllegalArgumentException("Attribute cannot be null");
    }
    this.attributeName = attribute;
}
