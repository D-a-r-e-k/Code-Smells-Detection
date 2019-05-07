/******************************************************************************/
/**
 * Sets an attribute in a CellView
 * 
 * @param view CellView, the attribute should be set
 * @param key The attribute will be stored in the CellView under that key.
 * @param obj Object representing the attribute, that should be stored.
 */
private void setAttribute(CellView view, String key, Object obj) {
    if (view.getAttributes() == null)
        view.changeAttributes(new AttributeMap());
    Map attributes = view.getAttributes();
    attributes.put(key, obj);
}
