private Parse GenerateCellParses(String[] cells, int cellIndex) {
    if (cellIndex >= cells.length)
        return null;
    return new Parse("td", cells[cellIndex], null, GenerateCellParses(cells, cellIndex + 1));
}
