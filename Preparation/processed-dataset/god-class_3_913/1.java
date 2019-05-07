public void printTotals(int currentRow, StringBuffer out) {
    // For each column, output: 
    List headerCells = tableModel.getHeaderCellList();
    if (firstRowOfCurrentSet < currentRow) // If there is more than one row, show a total 
    {
        out.append(totalsRowOpen);
        for (Iterator iterator = headerCells.iterator(); iterator.hasNext(); ) {
            HeaderCell headerCell = (HeaderCell) iterator.next();
            if (columnNumber == headerCell.getColumnNumber()) {
                // a totals label if it is the column for the current group 
                String currentLabel = getCellValue(columnNumber, firstRowOfCurrentSet);
                out.append(getTotalsTdOpen(headerCell, getTotalLabelClass() + " group-" + (columnNumber + 1)));
                out.append(getTotalRowLabel(currentLabel));
            } else if (headerCell.isTotaled()) {
                // a total if the column should be totaled 
                Object total = getTotalForColumn(headerCell.getColumnNumber(), firstRowOfCurrentSet, currentRow);
                out.append(getTotalsTdOpen(headerCell, getTotalValueClass() + " group-" + (columnNumber + 1)));
                out.append(formatTotal(headerCell, total));
            } else {
                // blank, if it is not a totals column 
                String style = "group-" + (columnNumber + 1);
                if (headerCell.getColumnNumber() < innermostGroup) {
                    style += " " + getTotalLabelClass() + " ";
                }
                out.append(getTotalsTdOpen(headerCell, style));
            }
            out.append(TagConstants.TAG_OPENCLOSING + TagConstants.TAGNAME_COLUMN + TagConstants.TAG_CLOSE);
        }
        out.append("\n</tr>\n");
    }
}
