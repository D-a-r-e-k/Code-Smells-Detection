boolean isEqualReadable(Session session, PersistentStore store, NodeAVL node) {
    NodeAVL c = node;
    Object[] data;
    Object[] nodeData;
    if (session.database.txManager.canRead(session, node.getRow(store), TransactionManager.ACTION_DUP, null)) {
        return true;
    }
    data = node.getData(store);
    while (true) {
        c = last(store, c);
        if (c == null) {
            break;
        }
        nodeData = c.getData(store);
        if (compareRow(session, data, nodeData) == 0) {
            Row row = c.getRow(store);
            if (session.database.txManager.canRead(session, row, TransactionManager.ACTION_DUP, null)) {
                return true;
            }
            continue;
        }
        break;
    }
    while (true) {
        c = next(session, store, node);
        if (c == null) {
            break;
        }
        nodeData = c.getData(store);
        if (compareRow(session, data, nodeData) == 0) {
            Row row = c.getRow(store);
            if (session.database.txManager.canRead(session, row, TransactionManager.ACTION_DUP, null)) {
                return true;
            }
            continue;
        }
        break;
    }
    return false;
}
