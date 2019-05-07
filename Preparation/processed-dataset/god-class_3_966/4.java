/**
     * Sets the <i>i</i><sup>th</sup> {@link Selectable} being selected.
     * @param i the zero-based index
     * @param sel the new {@link Selectable}
     * @throws IllegalStateException if I have already been resolved
     */
public void setSelect(int i, Selectable sel) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    _select.set(i, sel);
}
