/******************************************************************************/
/**
 * Returns an attribute from a CellView
 * 
 * @param view CellView, that stores the attribute
 * @param key The attribute is stored in the CellView with this key
 * @return Object stored with the given key in the given CellView
 */
private Object getAttribute(CellView view, String key) {
    return view.getAttributes().get(key);
}
