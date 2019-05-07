/**
     * <p>
     * Set the prefix that should be pre-pended to all table names.
     * </p>
     */
public void setTablePrefix(String prefix) {
    if (prefix == null) {
        prefix = "";
    }
    this.tablePrefix = prefix;
}
