/**
	 * @return Returns an unordered array of all hidden cellviews.
	 */
public CellView[] getHiddenCellViews() {
    Collection coll = hiddenMapping.values();
    CellView[] result = new CellView[coll.size()];
    coll.toArray(result);
    return result;
}
