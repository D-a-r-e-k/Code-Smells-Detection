/**
     * Responsible for handling tail of ALTER TABLE ... DROP CONSTRAINT ...
     */
void processAlterTableDropConstraint(Table table, String name, boolean cascade) {
    session.commit(false);
    TableWorks tableWorks = new TableWorks(session, table);
    tableWorks.dropConstraint(name, cascade);
    return;
}
