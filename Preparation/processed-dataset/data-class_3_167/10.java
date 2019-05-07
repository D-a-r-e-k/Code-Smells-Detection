/**
	 * @return Returns an unordered array of all visible cellviews.
	 */
public CellView[] getCellViews() {
    Collection coll = mapping.values();
    CellView[] result = new CellView[coll.size()];
    coll.toArray(result);
    return result;
}
