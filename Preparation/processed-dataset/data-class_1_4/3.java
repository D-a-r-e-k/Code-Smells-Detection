private Parse GenerateRowParses(String[] rows, int rowIndex) {
    if (rowIndex >= rows.length)
        return null;
    String[] cells = rows[rowIndex].split("\\]\\s*\\[");
    if (cells.length != 0) {
        cells[0] = cells[0].substring(1);
        // strip beginning '['  
        int lastCell = cells.length - 1;
        cells[lastCell] = cells[lastCell].replaceAll("\\]$", "");
    }
    return new Parse("tr", null, GenerateCellParses(cells, 0), GenerateRowParses(rows, rowIndex + 1));
}
