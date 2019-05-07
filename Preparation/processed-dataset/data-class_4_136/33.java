/******************************************************************************/
/**
 * Returns an attribute from a cell contained in a given list of CellViews.
 * 
 * @param index Identifies the cell. This is the index of the cell in 
 * the given list of CellViews
 * @param key Identifies the Attribute, that should be retrieved.
 * @param list List containing only CellViews
 */
private Object getAttribute(int index, String key, ArrayList list) {
    CellView view = (CellView) list.get(index);
    return view.getAttributes().get(key);
}
