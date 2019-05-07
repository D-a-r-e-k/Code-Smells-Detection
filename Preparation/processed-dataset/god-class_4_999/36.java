/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note </a> for more information.
	 */
protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    // Strings get interned... 
    if (propertyName == "text")
        super.firePropertyChange(propertyName, oldValue, newValue);
}
