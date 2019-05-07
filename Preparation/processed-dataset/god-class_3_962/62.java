protected List updateView() {
    ArrayList updatedList = new ArrayList();
    Iterator columnIterator = _columns.iterator();
    while (columnIterator.hasNext()) {
        LogTableColumn column = (LogTableColumn) columnIterator.next();
        JCheckBoxMenuItem result = getLogTableColumnMenuItem(column);
        // check and see if the checkbox is checked 
        if (result.isSelected()) {
            updatedList.add(column);
        }
    }
    return updatedList;
}
