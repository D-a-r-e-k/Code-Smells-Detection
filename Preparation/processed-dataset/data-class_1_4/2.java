private Parse GenerateTableParse(String table) throws ParseException {
    String[] rows = table.split("\n");
    return new Parse("table", null, GenerateRowParses(rows, 0), null);
}
