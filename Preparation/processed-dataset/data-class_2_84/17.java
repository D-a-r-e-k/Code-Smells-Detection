public void startOfGroup(String value, int group) {
    if (containsTotaledColumns) {
        StringBuffer tr = new StringBuffer();
        tr.append("<tr>");
        GroupTotals groupTotals = (GroupTotals) groupNumberToGroupTotal.get(new Integer(group));
        int myColumnNumber = groupTotals.columnNumber;
        for (int i = 0; i < myColumnNumber; i++) {
            tr.append("<td></td>\n");
        }
        tr.append("<td class=\"").append(getSubtotalHeaderClass()).append(" group-").append(group).append("\" >");
        tr.append(value).append("</td>\n");
        List headerCells = tableModel.getHeaderCellList();
        for (int i = myColumnNumber; i < headerCells.size() - 1; i++) {
            tr.append("<td></td>\n");
        }
        tr.append("</tr>\n");
        headerRows.add(tr);
    }
}
