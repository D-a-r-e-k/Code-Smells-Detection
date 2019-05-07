protected Object getTotalForColumn(int columnNumber, int startRow, int stopRow) {
    List fullList = tableModel.getRowListFull();
    List window = fullList.subList(startRow, stopRow + 1);
    Object total = null;
    for (Iterator iterator = window.iterator(); iterator.hasNext(); ) {
        Row row = (Row) iterator.next();
        ColumnIterator columnIterator = row.getColumnIterator(tableModel.getHeaderCellList());
        while (columnIterator.hasNext()) {
            Column column = columnIterator.nextColumn();
            if (column.getHeaderCell().getColumnNumber() == columnNumber) {
                Object value = null;
                try {
                    value = column.getValue(false);
                } catch (ObjectLookupException e) {
                    logger.error(e);
                } catch (DecoratorException e) {
                    logger.error(e);
                }
                if (value != null && !TagConstants.EMPTY_STRING.equals(value)) {
                    total = add(column, total, value);
                }
            }
        }
    }
    return total;
}
