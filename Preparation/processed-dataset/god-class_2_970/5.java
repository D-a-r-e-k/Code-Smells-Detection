/**
     * Adds an attribute {@link #SUGIYAMA_SELECTED SUGIYAMA_SELECTED} to the specified selected cell views.
     *
     * @param selectedCellViews the specified cell views
     * @param addMark true to add the mark, false to remove the mark
     */
protected void markSelected(CellView[] selectedCellViews, boolean addMark) {
    if (addMark) {
        for (int i = 0; i < selectedCellViews.length; i++) {
            if (selectedCellViews[i] != null) {
                selectedCellViews[i].getAttributes().put(SUGIYAMA_SELECTED, Boolean.TRUE);
            }
        }
    } else {
        for (int i = 0; i < selectedCellViews.length; i++) {
            if (selectedCellViews[i] != null) {
                selectedCellViews[i].getAttributes().remove(SUGIYAMA_SELECTED);
            }
        }
    }
}
