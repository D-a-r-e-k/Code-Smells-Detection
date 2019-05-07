public void doRows(Parse rows) {
    Table.table = new Parse("table", null, copy(rows), null);
    // evaluate the rest of the table like a runner  
    (new Fixture()).doTables(Table.table);
}
