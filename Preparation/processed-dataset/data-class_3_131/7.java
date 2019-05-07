/**
     * Adds a {@link TableIdentifier} to the list
     * of tables being selected from.
     * @param table a {@link TableIdentifier}
     * @throws IllegalStateException if I have already been resolved
     */
public void addFrom(TableIdentifier table) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    if (_from == null) {
        _from = new FromNode();
        _from.setType(FromNode.TYPE_SINGLE);
    }
    if (_from.getLeft() == null) {
        _from.setLeft(table);
    } else if (_from.getRight() == null) {
        _from.setRight(table);
        _from.setType(FromNode.TYPE_INNER);
    } else {
        FromNode from = new FromNode();
        from.setLeft(_from);
        from.setRight(table);
        from.setType(FromNode.TYPE_INNER);
        _from = from;
    }
}
