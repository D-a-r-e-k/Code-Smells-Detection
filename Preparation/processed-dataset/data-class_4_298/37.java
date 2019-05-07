/**
     * Compare two rows of the table for inserting rows into unique indexes
     * Supports descending columns.
     *
     * @param session Session
     * @param newRow data
     * @param existingRow data
     * @param useRowId boolean
     * @param start int
     * @return comparison result, -1,0,+1
     */
int compareRowForInsertOrDelete(Session session, Row newRow, Row existingRow, boolean useRowId, int start) {
    Object[] a = newRow.getData();
    Object[] b = existingRow.getData();
    for (int j = start; j < colIndex.length; j++) {
        int i = colTypes[j].compare(session, a[colIndex[j]], b[colIndex[j]]);
        if (i != 0) {
            if (isSimpleOrder) {
                return i;
            }
            boolean nulls = a[colIndex[j]] == null || b[colIndex[j]] == null;
            if (colDesc[j] && !nulls) {
                i = -i;
            }
            if (nullsLast[j] && nulls) {
                i = -i;
            }
            return i;
        }
    }
    if (useRowId) {
        return newRow.getPos() - existingRow.getPos();
    }
    return 0;
}
