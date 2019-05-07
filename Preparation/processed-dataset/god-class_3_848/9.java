public void doRows(Parse rows) {
    while (rows != null) {
        Parse more = rows.more;
        doRow(rows);
        rows = more;
    }
}
