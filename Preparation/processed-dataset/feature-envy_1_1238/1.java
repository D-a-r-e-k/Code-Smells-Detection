private String dumpRows(Parse row) {
    String result = "";
    String separator = "";
    while (row != null) {
        result += separator;
        result += dumpCells(row.parts);
        separator = "\n";
        row = row.more;
    }
    return result;
}
