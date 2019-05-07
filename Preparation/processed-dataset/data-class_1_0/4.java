private String dumpTables(Parse table) {
    String result = "";
    String separator = "";
    while (table != null) {
        result += separator;
        result += dumpRows(table.parts);
        separator = "\n----\n";
        table = table.more;
    }
    return result;
}
