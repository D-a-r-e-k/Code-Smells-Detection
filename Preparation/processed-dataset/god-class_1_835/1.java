public void doRows(Parse rows) {
    actualRow = Table.table.parts;
    if (rows.size() != actualRow.size())
        throw new RuntimeException("wrong size table");
    super.doRows(rows);
}
