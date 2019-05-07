/**
     * Gets the <i>i</i><sup>th</sup> {@link Selectable} being selected.
     * Clients should treat the returned value as immutable.
     * @param i the zero-based index
     */
public Selectable getSelect(int i) {
    return (Selectable) (_select.get(i));
}
