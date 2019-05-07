/**
     * Insert a node into the index
     */
public void insert(Session session, PersistentStore store, Row row) {
    NodeAVL n;
    NodeAVL x;
    boolean isleft = true;
    int compare = -1;
    boolean compareRowId = !isUnique || hasNulls(row.getData());
    writeLock.lock();
    store.lock();
    try {
        n = getAccessor(store);
        x = n;
        if (n == null) {
            store.setAccessor(this, ((RowAVL) row).getNode(position));
            store.setElementCount(this, 1, 1);
            return;
        }
        while (true) {
            Row currentRow = n.getRow(store);
            compare = compareRowForInsertOrDelete(session, row, currentRow, compareRowId, 0);
            // after the first match and check, all compares are with row id 
            if (compare == 0 && session != null && !compareRowId && session.database.txManager.isMVRows()) {
                if (!isEqualReadable(session, store, n)) {
                    compareRowId = true;
                    compare = compareRowForInsertOrDelete(session, row, currentRow, compareRowId, colIndex.length);
                }
            }
            if (compare == 0) {
                Constraint c = null;
                if (isConstraint) {
                    c = ((Table) table).getUniqueConstraintForIndex(this);
                }
                if (c == null) {
                    throw Error.error(ErrorCode.X_23505, name.statementName);
                } else {
                    throw c.getException(row.getData());
                }
            }
            isleft = compare < 0;
            x = n;
            n = x.child(store, isleft);
            if (n == null) {
                break;
            }
        }
        x = x.set(store, isleft, ((RowAVL) row).getNode(position));
        balance(store, x, isleft);
        store.updateElementCount(this, 1, 1);
    } finally {
        store.unlock();
        writeLock.unlock();
    }
}
