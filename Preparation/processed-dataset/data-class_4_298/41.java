public void delete(Session session, PersistentStore store, Row row) {
    if (!row.isInMemory()) {
        row = (Row) store.get(row, false);
    }
    NodeAVL node = ((RowAVL) row).getNode(position);
    if (node != null) {
        delete(store, node);
        store.updateElementCount(this, -1, -1);
    }
}
