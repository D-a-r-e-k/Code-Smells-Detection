Result executeDeleteTruncateStatement(Session session) {
    PersistentStore store = targetTable.getRowStore(session);
    RowIterator it = targetTable.getPrimaryIndex().firstRow(store);
    try {
        while (it.hasNext()) {
            Row row = it.getNextRow();
            session.addDeleteAction((Table) row.getTable(), row, null);
        }
        if (restartIdentity && targetTable.identitySequence != null) {
            targetTable.identitySequence.reset();
        }
    } finally {
        it.release();
    }
    return Result.updateOneResult;
}
