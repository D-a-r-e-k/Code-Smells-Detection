/**
	 * Handles the removal of view local attributes. Since these attributes are
	 * only being stored in the view, the option is provided to copy the values
	 * for that key into the model. Without this, those values are lost.
	 * 
	 * @param key
	 *            the key of the view local attribute
	 * @param addToModel
	 *            whether or not to move the attribute values to the graph model
	 * @param override
	 *            whether or not to override the key's value in the model cell's
	 *            attribute map if it exists
	 * @return whether or not the operation completed sucessfully
	 */
public boolean removeViewLocalAttribute(Object key, boolean addToModel, boolean override) {
    if (localAttributes.contains(key)) {
        if (addToModel) {
            // Iterate through all views copying this attribute to the 
            // cell. 
            copyRemovedViewValue(key, addToModel, override, mapping.values());
            copyRemovedViewValue(key, addToModel, override, hiddenMapping.values());
        }
        localAttributes.remove(key);
        return true;
    }
    return false;
}
