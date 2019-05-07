boolean hasNulls(Object[] rowData) {
    for (int j = 0; j < colIndex.length; j++) {
        if (rowData[colIndex[j]] == null) {
            return true;
        }
    }
    return false;
}
