public void init(PageContext context, Object decorated, TableModel model) {
    super.init(context, decorated, model);
    List headerCells = model.getHeaderCellList();
    // go through each column, looking for grouped columns; add them to the group number map 
    for (Iterator iterator = headerCells.iterator(); iterator.hasNext(); ) {
        HeaderCell headerCell = (HeaderCell) iterator.next();
        containsTotaledColumns = containsTotaledColumns || headerCell.isTotaled();
        if (headerCell.getGroup() > 0) {
            groupNumberToGroupTotal.put(new Integer(headerCell.getGroup()), new GroupTotals(headerCell.getColumnNumber()));
            if (headerCell.getGroup() > innermostGroup) {
                innermostGroup = headerCell.getGroup();
            }
        }
    }
}
