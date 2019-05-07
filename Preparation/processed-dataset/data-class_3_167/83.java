/**
	 * Returns true if <code>key</code> is a control attribute
	 */
protected boolean isControlAttribute(Object cell, Object key, Object value) {
    return GraphConstants.REMOVEALL.equals(key) || GraphConstants.REMOVEATTRIBUTES.equals(key);
}
