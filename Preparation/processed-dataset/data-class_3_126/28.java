protected int findRecord(int startRow, String searchText, List records) {
    if (startRow < 0) {
        startRow = 0;
    } else {
        startRow++;
    }
    int len = records.size();
    for (int i = startRow; i < len; i++) {
        if (matches((LogRecord) records.get(i), searchText)) {
            return i;
        }
    }
    // wrap around to beginning if when we reach the end with no match 
    len = startRow;
    for (int i = 0; i < len; i++) {
        if (matches((LogRecord) records.get(i), searchText)) {
            return i;
        }
    }
    // nothing found 
    return -1;
}
