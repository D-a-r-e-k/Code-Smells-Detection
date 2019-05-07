/**
     * Issue a grand total row at the bottom.
     * @return the suitable string
     */
protected String totalAllRows() {
    if (containsTotaledColumns) {
        List headerCells = tableModel.getHeaderCellList();
        StringBuffer output = new StringBuffer();
        int currentRow = getListIndex();
        output.append(TagConstants.TAG_OPEN + TagConstants.TAGNAME_ROW + " class=\"grandtotal-row\"" + TagConstants.TAG_CLOSE);
        boolean first = true;
        for (Iterator iterator = headerCells.iterator(); iterator.hasNext(); ) {
            HeaderCell headerCell = (HeaderCell) iterator.next();
            if (first) {
                output.append(getTotalsTdOpen(headerCell, getGrandTotalLabel()));
                output.append(getGrandTotalDescription());
                first = false;
            } else if (headerCell.isTotaled()) {
                // a total if the column should be totaled 
                Object total = getTotalForColumn(headerCell.getColumnNumber(), 0, currentRow);
                output.append(getTotalsTdOpen(headerCell, getGrandTotalSum()));
                output.append(formatTotal(headerCell, total));
            } else {
                // blank, if it is not a totals column 
                output.append(getTotalsTdOpen(headerCell, getGrandTotalNoSum()));
            }
            output.append(TagConstants.TAG_OPENCLOSING + TagConstants.TAGNAME_COLUMN + TagConstants.TAG_CLOSE);
        }
        output.append("\n</tr>\n");
        return output.toString();
    } else {
        return "";
    }
}
