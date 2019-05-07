int compareObject(Session session, Object[] a, Object[] b, int[] rowColMap, int position) {
    return colTypes[position].compare(session, a[colIndex[position]], b[rowColMap[position]]);
}
