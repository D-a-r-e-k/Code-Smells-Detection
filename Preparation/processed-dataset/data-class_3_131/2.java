/**
     * Adds a {@link Selectable} to the list of items being selected.
     * @param column the {@link Selectable} to add
     * @throws IllegalStateException if I have already been resolved
     */
public void addSelect(Selectable column) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    _select.add(column);
}
