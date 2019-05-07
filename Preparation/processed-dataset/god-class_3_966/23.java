/** @deprecated */
private TableIdentifier[] getFromArray() {
    if (_from != null) {
        return (_from.toTableArray());
    }
    return (null);
}
