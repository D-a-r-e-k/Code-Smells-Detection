/**
     * Compares two table rows based on the columns of this index. The rowColMap
     * parameter specifies which columns of the other table are to be compared
     * with the colIndex columns of this index. The rowColMap can cover all or
     * only some columns of this index.
     *
     * @param session Session
     * @param a row from another table
     * @param rowColMap column indexes in the other table
     * @param b a full row in this table
     * @return comparison result, -1,0,+1
     */
public int compareRowNonUnique(Session session, Object[] a, Object[] b, int[] rowColMap) {
    int fieldcount = rowColMap.length;
    for (int j = 0; j < fieldcount; j++) {
        int i = colTypes[j].compare(session, a[colIndex[j]], b[rowColMap[j]]);
        if (i != 0) {
            return i;
        }
    }
    return 0;
}
