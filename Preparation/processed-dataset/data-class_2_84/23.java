protected String getCellValue(int columnNumber, int rowNumber) {
    List fullList = tableModel.getRowListFull();
    Row row = (Row) fullList.get(rowNumber);
    ColumnIterator columnIterator = row.getColumnIterator(tableModel.getHeaderCellList());
    while (columnIterator.hasNext()) {
        Column column = columnIterator.nextColumn();
        if (column.getHeaderCell().getColumnNumber() == columnNumber) {
            try {
                column.initialize();
                return column.getChoppedAndLinkedValue();
            } catch (ObjectLookupException e) {
                logger.error("Error: " + e.getMessage(), e);
                throw new RuntimeException("Error: " + e.getMessage(), e);
            } catch (DecoratorException e) {
                logger.error("Error: " + e.getMessage(), e);
                throw new RuntimeException("Error: " + e.getMessage(), e);
            }
        }
    }
    throw new RuntimeException("Unable to find column " + columnNumber + " in the list of columns");
}
