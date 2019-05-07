/**
     * Detects whether the specified cell has been marked selected.
     *
     * @see #markSelected(CellView[], boolean)
     *
     * @param cell the cell to inspect
     * @return true if the view has been marked selected and false otherwise.
     */
protected boolean isSelected(final GraphLayoutCache cache, final Object cell) {
    final CellView view = cache.getMapping(cell, false);
    return view != null && view.getAttributes().get(SUGIYAMA_SELECTED) != null;
}
