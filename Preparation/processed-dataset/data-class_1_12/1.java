public String ResultingHTML() throws Exception {
    Parse table = new Parse(OriginalHTML);
    Parse row = table.at(0, Row - 1);
    Parse cell = row.at(0, Column - 1);
    if (OverwriteCellBody != null)
        cell.body = OverwriteCellBody;
    if (AddToCellBody != null)
        cell.addToBody(AddToCellBody);
    if (OverwriteCellTag != null)
        cell.tag = OverwriteCellTag;
    if (OverwriteEndCellTag != null)
        cell.end = OverwriteEndCellTag;
    if (AddToCellTag != null)
        cell.addToTag(stripDelimiters(AddToCellTag));
    if (OverwriteRowTag != null)
        row.tag = OverwriteRowTag;
    if (OverwriteEndRowTag != null)
        row.end = OverwriteEndRowTag;
    if (AddToRowTag != null)
        row.addToTag(stripDelimiters(AddToRowTag));
    if (OverwriteTableTag != null)
        table.tag = OverwriteTableTag;
    if (OverwriteEndTableTag != null)
        table.end = OverwriteEndTableTag;
    if (AddToTableTag != null)
        table.addToTag(stripDelimiters(AddToTableTag));
    if (AddCellFollowing != null)
        addParse(cell, AddCellFollowing, new String[] { "td" });
    if (RemoveFollowingCell != null)
        removeParse(cell);
    if (AddRowFollowing != null)
        addParse(row, AddRowFollowing, new String[] { "tr", "td" });
    if (RemoveFollowingRow != null)
        removeParse(row);
    if (AddTableFollowing != null)
        addParse(table, AddTableFollowing, new String[] { "table", "tr", "td" });
    return GenerateOutput(table);
}
