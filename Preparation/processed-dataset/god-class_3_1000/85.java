/**
	 * Helper method to copy removed view local attributes to model cell's
	 * 
	 * @param key
	 *            the key of the view local attribute
	 * @param addToModel
	 *            whether or not to move the attribute values to the graph model
	 * @param override
	 *            whether or not to override the key's value in the model cell's
	 *            attribute map if it exists
	 * @param coll
	 *            the current collection being analysed
	 */
private void copyRemovedViewValue(Object key, boolean addToModel, boolean override, Collection coll) {
    Iterator iter = coll.iterator();
    while (iter.hasNext()) {
        CellView cellView = (CellView) iter.next();
        Map attributes = cellView.getAttributes();
        if (attributes.containsKey(key)) {
            if (addToModel) {
                Object cell = cellView.getCell();
                Map cellAttributes = graphModel.getAttributes(cell);
                if (cellAttributes != null) {
                    boolean cellContainsKey = cellAttributes.containsKey(key);
                    // Write the model cell's attribute map key 
                    // if overriding is enabled or if key doesn't 
                    // exist 
                    if (!override || !cellContainsKey) {
                        Object value = attributes.get(key);
                        cellAttributes.put(key, value);
                    }
                }
            }
            attributes.remove(key);
        }
    }
}
