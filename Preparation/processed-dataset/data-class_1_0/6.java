private String dumpCells(Parse cell) {
    String result = "";
    String separator = "";
    while (cell != null) {
        result += separator;
        result += "[" + cell.body + "]";
        separator = " ";
        cell = cell.more;
    }
    return result;
}
