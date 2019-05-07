/**
     * Process a bracketed column list as used in the declaration of SQL
     * CONSTRAINTS and return an array containing the indexes of the columns
     * within the table.
     *
     * @param table table that contains the columns
     * @param ascOrDesc boolean
     * @return array of column indexes
     */
private int[] readColumnList(Table table, boolean ascOrDesc) {
    OrderedHashSet set = readColumnNames(ascOrDesc);
    return table.getColumnIndexes(set);
}
