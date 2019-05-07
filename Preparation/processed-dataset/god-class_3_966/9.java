/**
     * Gets the <i>i</i><sup>th</sup> table being selected.
     * Clients should treat the returned value as immutable.
     * @param i the zero-based index
     */
public TableIdentifier getFrom(int i) {
    TableIdentifier[] tableIDs = _from.toTableArray();
    return (tableIDs[i]);
}
