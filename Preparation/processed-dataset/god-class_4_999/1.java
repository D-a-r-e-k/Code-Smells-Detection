/**
	 * Sets view to work with, caching necessary values until the next call of
	 * this method or until some other methods with explicitly specified
	 * different view
	 */
void setView(CellView value) {
    if (value instanceof EdgeView) {
        view = (EdgeView) value;
        installAttributes(view);
    } else {
        view = null;
    }
}
