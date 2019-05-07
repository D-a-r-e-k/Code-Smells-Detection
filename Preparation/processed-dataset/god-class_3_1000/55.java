/**
	 * Hook for subclassers to add more stuff for value changes. Currently this
	 * adds the new value to the change.
	 */
protected void augmentNestedMapForValueChange(Map nested, Object cell, Object newValue) {
    Map attrs = (Map) nested.get(cell);
    if (attrs != null)
        GraphConstants.setValue(attrs, newValue);
}
